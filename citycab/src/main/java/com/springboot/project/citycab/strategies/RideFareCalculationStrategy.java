package com.springboot.project.citycab.strategies;

import com.springboot.project.citycab.dto.RideRequestDTO;

public interface RideFareCalculationStrategy {

    double calculateFare(RideRequestDTO rideRequestDTO);

}
