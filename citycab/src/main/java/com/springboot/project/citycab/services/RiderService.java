package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RideDTO cancelRide(Long rideId, String reason);

    DriverDTO rateDriver(Long rideId, Integer rating);

    RiderDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    OtpDTO getOtp(Long rideId);

    Rider getCurrentRider();

    Page<CancelRideDTO> getCancelledRidesByRider(PageRequest pageRequest);
}
