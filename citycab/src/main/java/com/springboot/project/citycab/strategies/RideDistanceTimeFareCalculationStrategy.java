package com.springboot.project.citycab.strategies;


import com.springboot.project.citycab.dto.DistanceTimeFareDTO;
import com.springboot.project.citycab.entities.RideRequest;

public interface RideDistanceTimeFareCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;
    DistanceTimeFareDTO calculateDistanceTimeFare(RideRequest rideRequest);

}
