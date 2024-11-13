package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.RideRequestRepository;
import com.springboot.project.citycab.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    // Repository
    private final RideRequestRepository rideRequestRepository;

    @Transactional
    @Override
    public RideRequest saveRideRequest(RideRequest rideRequest) {
        return rideRequestRepository.save(rideRequest);
    }

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with rideRequestId: " + rideRequestId));
    }

    @Override
    public RideRequest getRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with rideRequestId: " + rideRequestId));
    }
}
