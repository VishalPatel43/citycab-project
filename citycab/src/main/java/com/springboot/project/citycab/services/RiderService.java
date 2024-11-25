package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RiderDTO getMyProfile();

    Rider saveRider(Rider rider);

    Rider createNewRider(User user);

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RideRequestDTO cancelRideRequestByRider(Long rideRequestId);

    List<DriverDTO> getAvailableDriversForRideRequest(Long rideRequestId);

    OtpDTO getOtp(Long rideId);

    RideDTO cancelRide(Long rideId, String reason);

    DriverDTO submitRating(Long rideId, RatingDTO ratingDTO);

    DistanceTimeResponseDTO driverToRiderDistanceTime(Long rideId, PointDTO driverLocation);

    DistanceTimeResponseDTO riderToDestinationDistanceTime(Long rideId, PointDTO driverLocation);

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Page<RatingDTO> getReviewsByRider(PageRequest pageRequest);

    Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest);

}
