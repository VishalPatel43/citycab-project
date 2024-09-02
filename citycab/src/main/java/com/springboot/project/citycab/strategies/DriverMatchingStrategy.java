package com.springboot.project.citycab.strategies;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.entities.Driver;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequestDTO rideRequestDTO);
    
}
