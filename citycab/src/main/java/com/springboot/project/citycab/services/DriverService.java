package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.CancelRideDTO;
import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import com.springboot.project.citycab.entities.Driver;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DriverService {


    RideDTO acceptRide(Long rideRequestId);

    RideDTO cancelRide(Long rideId, String reason);

    RideDTO startRide(Long rideId, String otp);

    RideDTO endRide(Long rideId);

    RiderDTO rateRider(Long rideId, Integer rating);

    DriverDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    List<Driver> findTenNearestDrivers(Point pickupLocation);

    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    Driver updateDriverAvailability(Driver driver, Boolean available);

    Page<CancelRideDTO> getCancelledRidesByDriver(PageRequest pageRequest);
}
