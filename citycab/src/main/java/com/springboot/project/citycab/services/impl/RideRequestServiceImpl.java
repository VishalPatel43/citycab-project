package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.RideRequestRepository;
import com.springboot.project.citycab.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    // Repository
    private final RideRequestRepository rideRequestRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public RideRequest saveRideRequest(RideRequest rideRequest) {
        return rideRequestRepository.save(rideRequest);
    }

    @Override
    public RideRequest getRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with rideRequestId: " + rideRequestId));
    }

    @Override
    public RideRequestDTO updateRideRequest(Long rideRequestId, RideRequestDTO rideRequestDTO) {
        findRideRequestById(rideRequestId);
        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);
        rideRequest.setRideRequestId(rideRequestId);
        return modelMapper.map(rideRequestRepository.save(modelMapper.map(rideRequestDTO, RideRequest.class)), RideRequestDTO.class);
    }

    @Override
    public RideRequestDTO findRideRequestById(Long rideRequestId) {
        RideRequest rideRequest = getRideRequestById(rideRequestId);
        return modelMapper.map(rideRequest, RideRequestDTO.class);
    }

    @Override
    public Page<RideRequestDTO> getAllRideRequests(PageRequest pageRequest) {
        return rideRequestRepository.findAll(pageRequest)
                .map(rideRequest -> modelMapper.map(rideRequest, RideRequestDTO.class));
    }
}
