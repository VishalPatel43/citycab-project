package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.RideRequest;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DriverService {


    RideDTO acceptRide(Long rideRequestId, PointDTO driverLocation);

    RideDTO cancelRide(Long rideId, String reason);

    RideDTO startRide(Long rideId, String otp);

    RideDTO endRide(Long rideId);

    DriverDTO getMyProfile();

    Driver getDriverById(Long driverId);

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriverAvailability(Driver driver, Boolean available);

    Page<CancelRideDTO> getCancelledRidesByDriver(PageRequest pageRequest);

    Driver saveDriver(Driver driver);

    Page<RatingDTO> getReviewsForDriver(PageRequest pageRequest);

    Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest);

    List<RideRequestDTO> getAvailableRideRequests();

    List<VehicleDTO> getVehiclesByDriverId();

    DriverDTO changeDriverLocation(PointDTO location);

    RideRequestDTO confirmAndClearAssociations(RideRequest request);

    RideRequestDTO cancelRideRequestByDriver(Long rideRequestId);

    List<Driver> findTenNearestDrivers(Point pickupLocation);

    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    List<Driver> findTopRatedDriversWithin2Km(Point pickupLocation);

    List<Driver> findHighestRatedDriversWithin3Km(Point pickupLocation);

    List<Driver> findDriversWithin3To10KmWithLowRating(Point pickupLocation);

    DriverDTO updateDriverAddress(Long driverId, AddressDTO addressDTO);

    Driver findDriverByAadharCardNumber(Long aadharCardNumber);

    Driver findDriverByDrivingLicenseNumber(String drivingLicenseNumber);

    DriverDTO currentDriverVehicle(VehicleDTO vehicleDTO);

    DriverDTO freeDriverVehicle();
}
