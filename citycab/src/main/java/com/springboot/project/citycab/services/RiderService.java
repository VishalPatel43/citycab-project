package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RideDTO cancelRide(Long rideId, String reason);

    DriverDTO submitRating(Long rideId, RatingDTO ratingDTO);

    RiderDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    OtpDTO getOtp(Long rideId);

    Rider getCurrentRider();

    Page<CancelRideDTO> getCancelledRidesByRider(PageRequest pageRequest);

    Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest);

}
