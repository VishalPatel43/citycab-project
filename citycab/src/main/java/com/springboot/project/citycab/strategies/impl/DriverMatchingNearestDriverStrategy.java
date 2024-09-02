package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    @Override
    public List<Driver> findMatchingDriver(RideRequestDTO rideRequestDTO) {
        return List.of();
    }
}
