package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import org.springframework.stereotype.Service;

@Service
public class RideFareSurgePricingRideFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequestDTO rideRequestDTO) {
        return 0;
    }
}
