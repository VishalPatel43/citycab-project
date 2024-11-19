package com.springboot.project.citycab.services;

import com.springboot.project.citycab.entities.RideRequest;

public interface RideRequestService {

    RideRequest saveRideRequest(RideRequest rideRequest);

    RideRequest findRideRequestById(Long rideRequestId);

    RideRequest getRideRequestById(Long rideRequestId);

}
