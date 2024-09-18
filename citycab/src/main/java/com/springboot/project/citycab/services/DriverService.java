package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.entities.Driver;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface DriverService {


    RideDTO acceptRide(Long rideRequestId);

    RideDTO cancelRide(Long rideId);

    RideDTO startRide(Long rideId, String otp);

    RideDTO endRide(Long rideId);

    RideDTO rateRider(Long rideId, Integer rating);

    DriverDTO getMyProfile();

    List<RideDTO> getAllMyRides();

    Driver getCurrentDriver();

    List<Driver> findTenNearestDrivers(Point pickupLocation);

    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

}
