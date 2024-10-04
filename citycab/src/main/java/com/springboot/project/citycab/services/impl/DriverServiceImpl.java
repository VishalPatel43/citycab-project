package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.CancelRideDTO;
import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RatingDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.entities.*;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.entities.enums.RideStatus;
import com.springboot.project.citycab.entities.enums.Role;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.DriverRepository;
import com.springboot.project.citycab.services.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    // Repository
    private final DriverRepository driverRepository;
    // Services
    private final RideRequestService rideRequestService;
    private final RideService rideService;
    private final PaymentService paymentService;
    private final CancelRideService cancelRideService;
    private final RatingService ratingService;
    // Mapper
    private final ModelMapper modelMapper;

    // After Ride Request if driver accept the ride then we return the RideDTO
    @Override
    @Transactional
    public RideDTO acceptRide(Long rideRequestId) {

        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING))
            throw new RuntimeException(
                    "RideRequest cannot be accepted, status is " + rideRequest.getRideRequestStatus()
            );

        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable())
            throw new RuntimeException("Driver cannot accept ride due to unavailability");

        // After that we create the ride
        // here we save the driver to make it unavailable so update the driver in the database
        Driver updatedDriver = updateDriverAvailability(currentDriver, false);

        Ride ride = rideService.createNewRide(rideRequest, updatedDriver);

        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
        rideDTO.setRating(null);
        return rideDTO;
    }

    @Override
    @Transactional
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
        updateDriverAvailability(driver, true);

        RideDTO rideDTO = modelMapper.map(cancelRide.getRide(), RideDTO.class);
        rideDTO.setRating(null);

        CancelRideDTO cancelRideDTO = modelMapper.map(cancelRide, CancelRideDTO.class);
        cancelRideDTO.setRide(null);
        rideDTO.setCancelRide(cancelRideDTO);

        return rideDTO;
    }

    @Override
    @Transactional
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
        return rideDTO;
    }

    @Override
    @Transactional
    public RideDTO endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver()))
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");

        if (!ride.getRideStatus().equals(RideStatus.ONGOING))
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: " + ride.getRideStatus());

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);

        updateDriverAvailability(driver, true);

        paymentService.processPayment(savedRide);

        Rating rating = ratingService.getRatingByRide(savedRide);
        RatingDTO ratingDTO = modelMapper.map(rating, RatingDTO.class);

        RideDTO rideDTO = modelMapper.map(savedRide, RideDTO.class);
        rideDTO.setRating(ratingDTO);
        return rideDTO;
    }

    @Override
    public DriverDTO getMyProfile() {
        Driver currentDiver = getCurrentDriver();
        return modelMapper.map(currentDiver, DriverDTO.class);
    }

    // List All the rides of the driver
    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService
                .getAllRidesOfDriver(currentDriver, pageRequest)
                .map(ride -> modelMapper.map(ride, RideDTO.class));
    }

    @Override
    public Driver getCurrentDriver() {
        return driverRepository
                .findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id" + 2L));
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

    @Override
    @Transactional
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

        return cancelledRides.map(cancelRide -> modelMapper.map(cancelRide, CancelRideDTO.class));
    }

    @Override
    @Transactional
    public Driver createNewDriver(Driver createDriver) {
        return driverRepository.save(createDriver);
    }

    @Override
    @Transactional
    public Driver updateDriver(Driver driver) {
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
}
