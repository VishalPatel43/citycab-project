package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.constants.enums.RideStatus;
import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.*;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.DriverRepository;
import com.springboot.project.citycab.services.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    // Repository
    private final DriverRepository driverRepository;
    // Services
    private final UserService userService;
    private final RideRequestService rideRequestService;
    private final RideService rideService;
    private final PaymentService paymentService;
    private final CancelRideService cancelRideService;
    private final AddressService addressService;
    private final VehicleService vehicleService;
    private RatingService ratingService;

    // Mapper
    private final ModelMapper modelMapper;

    @Autowired
    public void setRatingService(@Lazy RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // After Ride Request if driver accept the ride then we return the RideDTO
    @Transactional
    @Override
    public RideDTO acceptRide(Long rideRequestId) {

        // only when accept the rideRequest if Driver is present in the list of drivers of the rideRequest

        RideRequest rideRequest = rideRequestService.getRideRequestById(rideRequestId);

        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING))
            throw new RuntimeException(
                    "RideRequest cannot be accepted, status is " + rideRequest.getRideRequestStatus()
            );

        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable())
            throw new RuntimeException("Driver cannot accept ride due to unavailability");

        // Check if the current driver is in the list of drivers assigned to the ride request
        if (!rideRequest.getDrivers().contains(currentDriver))
            throw new RuntimeException("Driver is not assigned to this RideRequest");

        if (currentDriver.getCurrentVehicle() == null)
            throw new RuntimeException("Driver does not have any vehicle associated");

        if (currentDriver.getCurrentVehicle().getAvailable()) // if it's already assign to driver with current vehicle
            throw new RuntimeException("Vehicle is not available");

        // After that we create the ride
        // here we save the driver to make it unavailable so update the driver in the database
//        Driver updatedDriver = updateDriverAvailability(currentDriver, false);

        confirmAndClearAssociations(rideRequest);

//        Ride ride = rideService.createNewRide(rideRequest, updatedDriver);
        Ride ride = rideService.createNewRide(rideRequest, currentDriver);

        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
        rideDTO.setRating(null);
        rideDTO.getDriver().setVehicles(null);

        return rideDTO;
    }

    @Transactional
    @Override
    public RideDTO cancelRide(Long rideId, String reason) {

        Ride ride = rideService.getRideById(rideId);

        // check this driver is the driver of this ride means owns this ride
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver()))
            throw new RuntimeException("Driver cannot start the ride as it has not been accepted earlier");

        // only CANCELLED ride if it is in CONFIRMED status otherwise now meaning of it if it is CANCELLED, ONGOING, ENDED
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());

        // Means Diver accept the ride now, so now he can cancel the ride
        CancelRide cancelRide = cancelRideService.cancelRide(
                ride,
                reason,
                Role.DRIVER
        );

//        riderService.updateRiderAvailability(ride.getRider(), true);
//        updateDriverAvailability(driver, true);

        confirmAndClearAssociations(ride.getRideRequest());

        RideDTO rideDTO = modelMapper.map(cancelRide.getRide(), RideDTO.class);
        rideDTO.setRating(null);

        CancelRideDTO cancelRideDTO = modelMapper.map(cancelRide, CancelRideDTO.class);
        cancelRideDTO.setRide(null);
        rideDTO.setCancelRide(cancelRideDTO);
        rideDTO.getDriver().setVehicles(null);

        return rideDTO;
    }

    @Transactional
    @Override
    public RideDTO startRide(Long rideId, String otp) {

        Ride ride = rideService.getRideById(rideId);
        // check this driver is the driver of this ride means owns this ride
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver()))
            throw new RuntimeException("Driver cannot start the ride as it has not been accepted earlier");

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride status is not CONFIRMED hence, status: " + ride.getRideStatus());

        if (!otp.equals(ride.getOtp()))
            throw new RuntimeException("Otp is not valid, otp: " + otp);

        ride.setStartedAt(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ONGOING);
        Ride savedRide = rideService.updateRide(ride);

        paymentService.createNewPayment(savedRide);
        Rating rating = ratingService.createNewRating(savedRide);

        RatingDTO ratingDTO = modelMapper.map(rating, RatingDTO.class);

//        savedRide.setRating(rating); // set the rating to the ride for dto
        RideDTO rideDTO = modelMapper.map(savedRide, RideDTO.class);
        rideDTO.setRating(ratingDTO);
        rideDTO.getDriver().setVehicles(null);
        return rideDTO;
    }

    @Transactional
    @Override
    public RideDTO endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver()))
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");

        if (!ride.getRideStatus().equals(RideStatus.ONGOING))
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: " + ride.getRideStatus());

        ride.setEndedAt(LocalDateTime.now());

        ride.getRider().setAvailable(true);
        ride.getDriver().setAvailable(true);

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);

//        riderService.updateRiderAvailability(savedRide.getRider(), true);
//        updateDriverAvailability(driver, true);

        paymentService.processPayment(savedRide);

        Rating rating = ratingService.getRatingByRide(savedRide);
        RatingDTO ratingDTO = modelMapper.map(rating, RatingDTO.class);

        RideDTO rideDTO = modelMapper.map(savedRide, RideDTO.class);
        rideDTO.setRating(ratingDTO);
        rideDTO.getDriver().setVehicles(null);
        return rideDTO;
    }

    @Override
    public DriverDTO getMyProfile() {
        Driver currentDiver = getCurrentDriver();
        return modelMapper.map(currentDiver, DriverDTO.class);
    }

    @Override
    public Driver getDriverById(Long driverId) {
        return driverRepository
                .findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));
    }

    // List All the rides of the driver
    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService
                .getAllRidesOfDriver(currentDriver, pageRequest)
                .map(ride -> {
                    RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
                    rideDTO.getDriver().setVehicles(null);
                    return rideDTO;
                });
    }

    @Override
    public Driver getCurrentDriver() {
        User currentUser = userService.getCurrentUser();

        return driverRepository
                .findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not Associated with the current user id: " + currentUser.getUserId()));
    }

    @Override
    public List<Driver> findTenNearestDrivers(Point pickupLocation) {
        // If DriverDTO
//        List<Driver> drivers = driverRepository.findTenNearbyTopRatedDrivers(pickupLocation); //return drivers.stream().map(driver -> modelMapper.map(driver, DriverDTO.class)).collect(Collectors.toList());
        return driverRepository.findTenNearestDrivers(pickupLocation);
    }

    @Override
    public List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation) {
        return driverRepository.findTenNearbyTopRatedDrivers(pickupLocation);
    }

    @Override
    public List<Driver> findTopRatedDriversWithin2Km(Point pickupLocation) {
        return driverRepository.findTopRatedDriversWithin2Km(pickupLocation);
    }

    @Override
    public List<Driver> findHighestRatedDriversWithin3Km(Point pickupLocation) {
        return driverRepository.findHighestRatedDriversWithin3Km(pickupLocation);
    }

    @Override
    public List<Driver> findDriversWithin3To10KmWithLowRating(Point pickupLocation) {
        return driverRepository.findNearestDriversFrom3To10KmWithLowRating(pickupLocation);
    }

    @Transactional
    @Override
    public DriverDTO updateDriverAddress(Long driverId, AddressDTO addressDTO) {
        Driver driver = getDriverById(driverId);
        Driver currentDriver = getCurrentDriver();

        User currentUser = userService.getCurrentUser();

        if (!driver.getDriverId().equals(currentDriver.getDriverId()) && !currentUser.getRoles().contains(Role.ADMIN))
            throw new RuntimeException("You are not authorized to update profile for this driver");

        Address address = driver.getAddress();
        Address existingAddress = addressService.findAddressById(address.getAddressId());
        modelMapper.map(addressDTO, existingAddress);

        addressService.saveAddress(existingAddress);

        return modelMapper.map(driver, DriverDTO.class);
    }

    @Override
    public Driver findDriverByAadharCardNumber(Long aadharCardNumber) {
        return driverRepository.findByAadharCardNumber(aadharCardNumber).orElse(null);
    }

    @Override
    public Driver findDriverByDrivingLicenseNumber(String drivingLicenseNumber) {
        return driverRepository.findByDrivingLicenseNumber(drivingLicenseNumber).orElse(null);
    }

    @Transactional
    @Override
    public DriverDTO setCurrentDriverVehicle(VehicleDTO vehicleDTO) {
        Driver driver = getCurrentDriver();

        Vehicle vehicleServiceByRegistrationNumber = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());
        if (vehicleServiceByRegistrationNumber == null)
            throw new ResourceNotFoundException("Vehicle not found with registration number: " + vehicleDTO.getRegistrationNumber());

        Vehicle vehicleServiceByNumberPlate = vehicleService.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicleServiceByNumberPlate == null)
            throw new ResourceNotFoundException("Vehicle not found with number plate: " + vehicleDTO.getNumberPlate());

        if (!vehicleServiceByNumberPlate.getVehicleId().equals(vehicleServiceByRegistrationNumber.getVehicleId()))
            throw new RuntimeException("Vehicle not found with registration number: " + vehicleDTO.getRegistrationNumber());

        // check if the vehicle is already associated with the driver
        if (!driver.getVehicles().contains(vehicleServiceByNumberPlate))
            throw new RuntimeException("Vehicle is not associated with the driver");

//        vehicleService.updateVehicleAvailability(vehicleServiceByNumberPlate, false);
        driver.setCurrentVehicle(vehicleServiceByNumberPlate);
        driver.getCurrentVehicle().setAvailable(false);

        return modelMapper.map(driverRepository.save(driver), DriverDTO.class);
    }

    @Transactional
    @Override
    public DriverDTO freeDriverVehicle() {
        Driver driver = getCurrentDriver();

        Vehicle currentVehicle = driver.getCurrentVehicle();
        if (currentVehicle == null)
            throw new ResourceNotFoundException("Driver does not have any vehicle associated");

        Vehicle vehicle = vehicleService.findVehicleById(currentVehicle.getVehicleId());
        vehicleService.updateVehicleAvailability(vehicle, true);
//        driver.getCurrentVehicle().setAvailable(true);
        driver.setCurrentVehicle(null);

        return modelMapper.map(driverRepository.save(driver), DriverDTO.class);
    }

    @Transactional
    @Override
    public Driver updateDriverAvailability(Driver driver, Boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Page<CancelRideDTO> getCancelledRidesByDriver(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();

        if (currentDriver == null)
            throw new ResourceNotFoundException("Driver not found");
        // Fetch cancelled rides for the current driver
        Page<CancelRide> cancelledRides = cancelRideService.getCancelRideByRole(Role.DRIVER, pageRequest);

        return cancelledRides.map(cancelRide -> {
            CancelRideDTO cancelRideDTO = modelMapper.map(cancelRide, CancelRideDTO.class);
            cancelRideDTO.getRide().getDriver().setVehicles(null);
            return cancelRideDTO;
        });
    }

    @Transactional
    @Override
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Page<RatingDTO> getReviewsForDriver(PageRequest pageRequest) {

        Driver currentDriver = getCurrentDriver();

        if (currentDriver == null)
            throw new ResourceNotFoundException("Driver not found");

        return ratingService.getReviewsForDriver(currentDriver, pageRequest);
    }

    @Override
    public Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest) {

        Page<Driver> drivers = driverRepository
                .findByUserNameContainingIgnoreCase(name, pageRequest);

        if (drivers.isEmpty())
            throw new ResourceNotFoundException("No driver found with name: " + name);

        return drivers.map(driver -> modelMapper.map(driver, DriverDTO.class));
    }

    @Override
    public List<RideRequestDTO> getAvailableRideRequests() {
        Driver driver = getCurrentDriver();

        List<RideRequest> rideRequestList = driver.getRideRequests();

        return rideRequestList.stream()
                .map(rideRequest -> modelMapper.map(rideRequest, RideRequestDTO.class))
                .toList();
    }

    @Override
    public List<VehicleDTO> getVehiclesByDriverId() {
        Driver currentDriver = getCurrentDriver();

        Set<Vehicle> vehicles = currentDriver.getVehicles();

        return vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .toList();
    }

    @Transactional
    @Override
    public DriverDTO changeDriverLocation(PointDTO location) {
        Driver currentDriver = getCurrentDriver();

        Point point = modelMapper.map(location, Point.class);
        currentDriver.setCurrentLocation(point);

        return modelMapper.map(driverRepository.save(currentDriver), DriverDTO.class);
    }

    @Transactional
    @Override
    public RideRequestDTO confirmAndClearAssociations(RideRequest request) {
        RideRequest rideRequest = rideRequestService.getRideRequestById(request.getRideRequestId());

        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.CONFIRMED)) {
            // Clear the drivers list in the RideRequest
            List<Driver> drivers = rideRequest.getDrivers();
            for (Driver driver : drivers)
                driver.getRideRequests().remove(rideRequest); // Remove the ride request from each driver
            drivers.clear(); // Clear the list of drivers in RideRequest

            // Save the changes to the repositories
            rideRequest = rideRequestService.saveRideRequest(rideRequest);

        } else throw new RuntimeException("RideRequest is already confirmed");

        return modelMapper.map(rideRequest, RideRequestDTO.class);

    }

    @Transactional
    @Override
    public RideRequestDTO cancelRideRequestByDriver(Long rideRequestId) {

        RideRequest rideRequest = rideRequestService.getRideRequestById(rideRequestId);
        Driver currentDriver = getCurrentDriver();

        if (!rideRequest.getDrivers().contains(currentDriver))
            throw new RuntimeException("Driver is not associated with the ride request");

        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING))
            throw new RuntimeException("RideRequest cannot be cancelled, status is " + rideRequest.getRideRequestStatus());

        rideRequest.getDrivers().remove(currentDriver);
//        currentDriver.getRideRequests().remove(rideRequest);

        rideRequest = rideRequestService.saveRideRequest(rideRequest);
//        driverRepository.save(currentDriver);

        return modelMapper.map(rideRequest, RideRequestDTO.class);

    }
}
