package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.entities.enums.RideStatus;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.DriverRepository;
import com.springboot.project.citycab.services.DriverService;
import com.springboot.project.citycab.services.RideRequestService;
import com.springboot.project.citycab.services.RideService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
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
        currentDriver.setAvailable(false);
        // here we save the driver to make it unavailable so update the driver in the database
        Driver updatedDriver = driverRepository.save(currentDriver);

        Ride ride = rideService.createNewRide(rideRequest, updatedDriver);
        return modelMapper.map(ride, RideDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        return null;
    }

    @Transactional
    @Override
    public RideDTO startRide(Long rideId, String otp) {

        Ride ride = rideService.getRideById(rideId);
        // check this driver is the driver of this ride means owns this ride
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver()))
            throw new RuntimeException("Driver cannot start the ride as it has not been accepted earlier");
//            throw new RuntimeException("Driver does not own this ride with id: " + rideId);

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
//            throw new RuntimeException("Ride cannot be started, invalid status: " + ride.getRideStatus());
            throw new RuntimeException("Ride status is not CONFIRMED hence, status: " + ride.getRideStatus());

        if (!otp.equals(ride.getOtp()))
            throw new RuntimeException("Otp is not valid, otp: " + otp);

        ride.setStartedAt(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ONGOING);
//        Ride saveRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);
        Ride saveRide = rideService.updateRide(ride);

        return modelMapper.map(saveRide, RideDTO.class);
    }

    @Override
    public RideDTO endRide(Long rideId) {
        return null;
    }

    @Override
    public RideDTO rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public DriverDTO getMyProfile() {
        return null;
    }

    @Override
    public List<RideDTO> getAllMyRides() {
        return List.of();
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
//        List<Driver> drivers = driverRepository.findTenNearbyTopRatedDrivers(pickupLocation);
//        return drivers.stream()
//                .map(driver -> modelMapper.map(driver, DriverDTO.class))
//                .collect(Collectors.toList());
        return driverRepository.findTenNearestDrivers(pickupLocation);
    }

    @Override
    public List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation) {
        return driverRepository.findTenNearbyTopRatedDrivers(pickupLocation);
    }
}
