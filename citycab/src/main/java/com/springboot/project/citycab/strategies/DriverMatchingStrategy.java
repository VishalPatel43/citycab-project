package com.springboot.project.citycab.strategies;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.RideRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);

}
