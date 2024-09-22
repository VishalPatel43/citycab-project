package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.entities.enums.RideStatus;
import com.springboot.project.citycab.entities.enums.Role;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.DriverRepository;
import com.springboot.project.citycab.services.DriverService;
import com.springboot.project.citycab.services.RideRequestService;
import com.springboot.project.citycab.services.RideService;
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
        return modelMapper.map(ride, RideDTO.class);
    }

    @Override
    @Transactional
    public RideDTO cancelRide(Long rideId) {

        Ride ride = rideService.getRideById(rideId);

        // check this driver is the driver of this ride means owns this ride
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver()))
            throw new RuntimeException("Driver cannot start the ride as it has not been accepted earlier");

        // only CANCELLED ride if it is in CONFIRMED status otherwise now meaning of it if it is CANCELLED, ONGOING, ENDED
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());

        // Means Diver accept the ride now, so now he can cancel the ride
        ride.setRideStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancelledBy(Role.DRIVER);
        Ride updatedRide = rideService.updateRide(ride);

        updateDriverAvailability(driver, true);

        return modelMapper.map(updatedRide, RideDTO.class);
    }

    @Override
    @Transactional
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

        return modelMapper.map(savedRide, RideDTO.class);
    }

    @Override
    @Transactional
    public RideDTO rateRider(Long rideId, Integer rating) {
        return null;
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
    @Transactional
    public Driver updateDriverAvailability(Driver driver, Boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }
}
