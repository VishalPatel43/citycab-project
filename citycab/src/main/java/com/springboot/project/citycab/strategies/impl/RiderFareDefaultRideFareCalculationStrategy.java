package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.services.DistanceService;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import com.springboot.project.citycab.strategies.manager.DistanceServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderFareDefaultRideFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceServiceManager distanceServiceManager;

    @Override
    public double calculateFare(RideRequest rideRequest) {

        double distance = distanceServiceManager.calculateDistance(rideRequest.getPickupLocation(),
                rideRequest.getDropOffLocation());
        return distance * RIDE_FARE_MULTIPLIER;

//        return 10.0 * RIDE_FARE_MULTIPLIER;
    }
}
