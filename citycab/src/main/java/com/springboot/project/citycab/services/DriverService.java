package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DriverService {

    Driver getDriverById(Long driverId);

    DriverDTO findDriverById(Long driverId);

    Driver saveDriver(Driver driver);

    DriverDTO saveDriver(Long driverId, DriverDTO driverDTO);

    Page<DriverDTO> findAllDrivers(PageRequest pageRequest);

    Driver createDriver(User user, Address address, Vehicle vehicle, OnboardDriverDTO onboardDriverDTO, Point currentLocation);

    DriverDTO mapDriverToDTO(Driver driver);

    DriverDTO getMyProfile();

    DriverDTO setCurrentDriverVehicle(VehicleDTO vehicleDTO);

    List<RideRequestDTO> getAvailableRideRequests();

    RideDTO acceptRide(Long rideRequestId);

    RideRequestDTO cancelRideRequestByDriver(Long rideRequestId);

    RideDTO startRide(Long rideId, String otp);

    RideDTO cancelRide(Long rideId, String reason);

    RideDTO endRide(Long rideId);

    DriverDTO changeDriverLocation(PointDTO location);

    DriverDTO freeDriverVehicle();

    DriverDTO updateDriverAddress(AddressDTO addressDTO);

    Driver findDriverByAadhaarCardNumber(Long aadhaarCardNumber);

    Driver findDriverByDrivingLicenseNumber(String drivingLicenseNumber);

    RideRequestDTO confirmAndClearAssociations(RideRequest request);

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Page<RatingDTO> getReviewsForDriver(PageRequest pageRequest);

    List<VehicleDTO> getVehiclesByDriverId();

    List<Driver> findTopRatedDriversWithin2Km(Point pickupLocation);

    List<Driver> findHighestRatedDriversWithin3Km(Point pickupLocation);

    List<Driver> findDriversWithin3To10KmWithLowRating(Point pickupLocation);

    List<Driver> findTenNearestDrivers(Point pickupLocation);

    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest);


}
