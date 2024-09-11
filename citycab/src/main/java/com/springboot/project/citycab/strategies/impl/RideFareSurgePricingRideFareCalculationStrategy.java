package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import org.springframework.stereotype.Service;

//@Service
public class RideFareSurgePricingRideFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequest rideRequest) {
        return 0;
    }
}
