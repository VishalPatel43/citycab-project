package com.springboot.project.citycab.strategies;


import com.springboot.project.citycab.entities.RideRequest;

public interface RideFareCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;
    double calculateFare(RideRequest rideRequest);

}
