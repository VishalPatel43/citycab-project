package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.dto.DistanceTimeFareDTO;
import com.springboot.project.citycab.dto.DistanceTimeResponseDTO;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.strategies.RideDistanceTimeFareCalculationStrategy;
import com.springboot.project.citycab.strategies.manager.DistanceTimeServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingRideDistanceTimeFareCalculationStrategy implements RideDistanceTimeFareCalculationStrategy {

    private final DistanceTimeServiceManager distanceTimeServiceManager;
    private static final double SURGE_FACTOR = 1.5;

    /*
        We can put many conditions here such as for weather, traffic, etc.
        various strategies to calculate for the surge pricing
        We can also use the third party API to get the surge pricing --> Find Rain and change the surge factor
        Night time we can change the surge factor
        Availability of the drivers can also change the surge factor
    */

    // Here Implement the Time, Distance, and Fare Calculation Strategy
    @Override
    public DistanceTimeFareDTO calculateDistanceTimeFare(RideRequest rideRequest) {

        DistanceTimeResponseDTO responseDTO = distanceTimeServiceManager.calculateDistanceTime(rideRequest.getPickupLocation(),
                rideRequest.getDropOffLocation());

        double distance = responseDTO.getDistanceKm();
        double time = responseDTO.getTimeMinutes();
        double fare = distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;

        return new DistanceTimeFareDTO(distance, time, fare);
    }
}
