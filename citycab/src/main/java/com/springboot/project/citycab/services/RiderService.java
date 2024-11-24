package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RideDTO cancelRide(Long rideId, String reason);

    DriverDTO submitRating(Long rideId, RatingDTO ratingDTO);

    RiderDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Page<RatingDTO> getReviewsByRider(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider updateRider(Rider rider);

    OtpDTO getOtp(Long rideId);

    Rider getCurrentRider();

    Page<CancelRideDTO> getCancelledRidesByRider(PageRequest pageRequest);

    Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest);

    List<DriverDTO> getAvailableDriversForRideRequest(Long rideRequestId);

    RideRequestDTO cancelRideRequestByRider(Long rideRequestId);

    DistanceTimeResponseDTO driverToRiderDistanceTime(Long rideId, PointDTO driverLocation);

    DistanceTimeResponseDTO riderToDestinationDistanceTime(Long rideId, PointDTO driverLocation);
}
