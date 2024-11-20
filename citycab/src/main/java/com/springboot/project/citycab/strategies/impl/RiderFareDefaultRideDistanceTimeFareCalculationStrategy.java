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
public class RiderFareDefaultRideDistanceTimeFareCalculationStrategy implements RideDistanceTimeFareCalculationStrategy {

    private final DistanceTimeServiceManager distanceTimeServiceManager;

    // Here Implement the Time, Distance, and Fare Calculation Strategy
    @Override
    public DistanceTimeFareDTO calculateDistanceTimeFare(RideRequest rideRequest) {

        DistanceTimeResponseDTO responseDTO = distanceTimeServiceManager.calculateDistanceTime(rideRequest.getPickupLocation(),
                rideRequest.getDropOffLocation());

        double distance = responseDTO.getDistanceKm();
        double time = responseDTO.getTimeMinutes();
        double fare = distance * RIDE_FARE_MULTIPLIER;

        return new DistanceTimeFareDTO(distance, time, fare);

//        return 10.0 * RIDE_FARE_MULTIPLIER;
    }
}
