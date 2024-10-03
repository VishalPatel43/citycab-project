package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import com.springboot.project.citycab.strategies.manager.DistanceServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingRideFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceServiceManager distanceServiceManager;
    private static final double SURGE_FACTOR = 1.5;

    /*
        We can put many conditions here such as for weather, traffic, etc.
        various strategies to calculate for the surge pricing
        We can also use the third party API to get the surge pricing --> Find Rain and change the surge factor
        Night time we can change the surge factor
        Availability of the drivers can also change the surge factor
    */

    @Override
    public double calculateFare(RideRequest rideRequest) {

        double distance = distanceServiceManager.calculateDistance(rideRequest.getPickupLocation(),
                rideRequest.getDropOffLocation());

        return distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
//        return 10.0 * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
    }
}
