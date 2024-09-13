package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.repositories.RideRequestRepository;
import com.springboot.project.citycab.repositories.RiderRepository;
import com.springboot.project.citycab.services.RiderService;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideFareCalculationStrategy rideFareCalculationStrategy;
    private final DriverMatchingStrategy driverMatchingStrategy;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    @Override
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {

        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);
//        log.info("Ride request received: {}", rideRequest);
//        log.info(rideRequest.toString());

        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

        Double fare = rideFareCalculationStrategy.calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        // broadcast the ride request to all drivers
        // find the matching driver
        driverMatchingStrategy.findMatchingDriver(rideRequest);
        return modelMapper.map(savedRideRequest, RideRequestDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDTO getMyProfile() {
        return null;
    }

    @Override
    public List<RideDTO> getAllMyRides() {
        return List.of();
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }
}