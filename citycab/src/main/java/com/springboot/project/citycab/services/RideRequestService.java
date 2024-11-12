package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.entities.RideRequest;

import java.util.List;

public interface RideRequestService {

    RideRequest saveRideRequest(RideRequest rideRequest);

    RideRequest findRideRequestById(Long rideRequestId);

    RideRequest getRideRequestById(Long rideRequestId);

}
