package com.springboot.project.citycab.services;


import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    // This RideService can use by other services such as RiderService, DriverService, etc.

    Ride getRideById(Long rideId); // Internal service so we can return entity

    void matchWithDrivers(RideRequestDTO rideRequestDTO);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    // Any Service can use it
    Ride updateRideStatus(Ride ride, RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Long riderId, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest);

    Ride updateRide(Ride ride);
}
