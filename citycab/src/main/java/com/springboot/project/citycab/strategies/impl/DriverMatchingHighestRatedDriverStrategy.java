package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return List.of();
    }
}
