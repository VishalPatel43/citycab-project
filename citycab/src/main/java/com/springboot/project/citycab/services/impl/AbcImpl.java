package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.constants.enums.RideStatus;
import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.*;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.DriverRepository;
import com.springboot.project.citycab.services.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AbcImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserService userService;
    private final RideRequestService rideRequestService;
    private final RideService rideService;
    private final PaymentService paymentService;
    private final CancelRideService cancelRideService;
    private final AddressService addressService;
    private final VehicleService vehicleService;
    private RatingService ratingService;
    private final ModelMapper modelMapper;

    private static final String DRIVER_NOT_FOUND = "Driver not found with id: ";
    private static final String UNAUTHORIZED_DRIVER = "Driver is not authorized for this operation.";

    @Autowired
    public void setRatingService(@Lazy RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Driver getDriverById(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(DRIVER_NOT_FOUND + driverId));
    }

    @Transactional
    @Override
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Transactional
    @Override
    public Driver createDriver(User user, Address address, Vehicle vehicle, OnboardDriverDTO onboardDriverDTO, Point currentLocation) {
        validateDriverUniqueness(onboardDriverDTO);

        Driver driver = Driver.builder()
                .user(user)
                .avgRating(0.0)
                .available(false)
                .address(address)
                .vehicles(new HashSet<>(Set.of(vehicle)))
                .currentLocation(currentLocation)
                .aadhaarCardNumber(onboardDriverDTO.getAadhaarCardNumber())
                .drivingLicenseNumber(onboardDriverDTO.getDrivingLicenseNumber())
                .build();

        driver.getUser().setRoles(Set.of(Role.DRIVER));
        return driverRepository.save(driver);
    }

    @Override
    public DriverDTO getMyProfile() {
        return mapDriverToDTO(getCurrentDriver());
    }

    @Override
    public DriverDTO mapDriverToDTO(Driver driver) {
        DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
        driverDTO.setVehicles(driver.getVehicles().stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toSet()));
        return driverDTO;
    }

    @Transactional
    @Override
    public DriverDTO setCurrentDriverVehicle(VehicleDTO vehicleDTO) {
        Driver driver = getCurrentDriver();
        Vehicle vehicle = vehicleService.validateExistingVehicle(vehicleDTO);

        driver.setCurrentVehicle(vehicle);
        vehicle.setAvailable(false);
        driver.setAvailable(true);

        return mapDriverToDTO(driverRepository.save(driver));
    }

    @Override
    public List<RideRequestDTO> getAvailableRideRequests() {
        return getCurrentDriver().getRideRequests().stream()
                .map(rideRequest -> modelMapper.map(rideRequest, RideRequestDTO.class))
                .toList();
    }

    @Transactional
    @Override
    public RideDTO acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.getRideRequestById(rideRequestId);
        validateRideRequestForAcceptance(rideRequest);

        Driver currentDriver = getCurrentDriver();
        confirmAndClearAssociations(rideRequest);

        Ride ride = rideService.createNewRide(rideRequest, currentDriver);
        return mapRideToDTO(ride);
    }

    @Transactional
    @Override
    public RideRequestDTO cancelRideRequestByDriver(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.getRideRequestById(rideRequestId);
        Driver driver = getCurrentDriver();

        validateRideRequestForCancellation(rideRequest, driver);

        rideRequest.getDrivers().remove(driver);
        return modelMapper.map(rideRequestService.saveRideRequest(rideRequest), RideRequestDTO.class);
    }

    @Transactional
    @Override
    public RideDTO startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        validateRideOwnership(ride);
        validateRideForStart(ride, otp);

        ride.setStartedAt(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ONGOING);

        Ride savedRide = rideService.saveRide(ride);
        paymentService.createNewPayment(savedRide);
        RatingDTO ratingDTO = modelMapper.map(ratingService.createNewRating(savedRide), RatingDTO.class);

        return mapRideWithRating(savedRide, ratingDTO);
    }

    @Transactional
    @Override
    public RideDTO cancelRide(Long rideId, String reason) {
        Ride ride = rideService.getRideById(rideId);
        validateRideOwnership(ride);

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());

        CancelRide cancelRide = cancelRideService.cancelRide(ride, reason, Role.DRIVER);
        confirmAndClearAssociations(ride.getRideRequest());

        return mapRideToDTO(cancelRide.getRide());
    }

    @Transactional
    @Override
    public RideDTO endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        validateRideOwnership(ride);

        if (!ride.getRideStatus().equals(RideStatus.ONGOING))
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: " + ride.getRideStatus());

        ride.setEndedAt(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ENDED);
        ride.getRider().setAvailable(true);
        ride.getDriver().setAvailable(true);

        Ride savedRide = rideService.saveRide(ride);
        paymentService.processPayment(savedRide);

        RatingDTO ratingDTO = modelMapper.map(ratingService.getRatingByRide(savedRide), RatingDTO.class);
        return mapRideWithRating(savedRide, ratingDTO);
    }

    @Override
    public DriverDTO changeDriverLocation(PointDTO location) {
        Driver driver = getCurrentDriver();
        driver.setCurrentLocation(modelMapper.map(location, Point.class));
        return mapDriverToDTO(driverRepository.save(driver));
    }

    @Transactional
    @Override
    public DriverDTO freeDriverVehicle() {
        Driver driver = getCurrentDriver();

        Vehicle currentVehicle = driver.getCurrentVehicle();
        if (currentVehicle == null)
            throw new ResourceNotFoundException("Driver does not have any vehicle associated");

        vehicleService.updateVehicleAvailability(currentVehicle, true);
        driver.setCurrentVehicle(null);

        return mapDriverToDTO(driverRepository.save(driver));
    }

    @Transactional
    @Override
    public DriverDTO updateDriverAddress(AddressDTO addressDTO) {
        Driver driver = getCurrentDriver();
        Address address = driver.getAddress();

        modelMapper.map(addressDTO, addressService.findAddressById(address.getAddressId()));
        addressService.saveAddress(address);

        return mapDriverToDTO(driver);
    }

    private void validateDriverUniqueness(OnboardDriverDTO onboardDriverDTO) {
        if (findDriverByAadhaarCardNumber(onboardDriverDTO.getAadhaarCardNumber()) != null)
            throw new RuntimeConflictException("Driver with Aadhaar Card Number already exists.");
        if (findDriverByDrivingLicenseNumber(onboardDriverDTO.getDrivingLicenseNumber()) != null)
            throw new RuntimeConflictException("Driver with Driving License Number already exists.");
    }

    private void validateRideRequestForAcceptance(RideRequest rideRequest) {
        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING))
            throw new RuntimeException("RideRequest cannot be accepted due to its current status: " + rideRequest.getRideRequestStatus());
    }

    private void validateRideOwnership(Ride ride) {
        if (!ride.getDriver().equals(getCurrentDriver()))
            throw new RuntimeException(UNAUTHORIZED_DRIVER);
    }

    private RideDTO mapRideToDTO(Ride ride) {
        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
        rideDTO.getDriver().setVehicles(null);
        return rideDTO;
    }

    private RideDTO mapRideWithRating(Ride ride, RatingDTO ratingDTO) {
        RideDTO rideDTO = mapRideToDTO(ride);
        rideDTO.setRating(ratingDTO);
        return rideDTO;
    }

    private Driver getCurrentDriver() {
        User currentUser = userService.getCurrentUser();
        return driverRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException(DRIVER_NOT_FOUND + currentUser.getUserId()));
    }
}
