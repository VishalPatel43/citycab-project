package com.springboot.project.citycab.services;


import com.springboot.project.citycab.dto.OtpDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface RideService {

    Ride getRideById(Long rideId); // Internal service so we can return entity

    RideDTO getRideDTOById(Long rideId); // External service so we can return DTO

    Page<RiderDTO> getAllRides(PageRequest pageRequest);

    RideDTO updateRide(Long rideId, RideDTO rideDTO);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);

    OtpDTO getOtp(Long rideId);

    Ride saveRide(Ride ride);

    Ride setDistanceTimeForRide(Ride ride);

}
