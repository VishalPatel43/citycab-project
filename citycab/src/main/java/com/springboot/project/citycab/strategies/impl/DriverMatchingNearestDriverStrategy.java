package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.services.DriverService;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverService driverService;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
//        return driverService.findTenNearestDrivers(rideRequest.getPickupLocation());
        return driverService.findDriversWithin3To10KmWithLowRating(rideRequest.getPickupLocation());
    }
}
