package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.entities.RideRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideRequestService {

    RideRequest saveRideRequest(RideRequest rideRequest);

    RideRequest getRideRequestById(Long rideRequestId);

    RideRequestDTO updateRideRequest(Long rideRequestId, RideRequestDTO rideRequestDTO);

    RideRequestDTO findRideRequestById(Long rideRequestId);

    Page<RideRequestDTO> getAllRideRequests(PageRequest pageRequest);

}
