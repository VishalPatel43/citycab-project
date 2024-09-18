package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.RiderRepository;
import com.springboot.project.citycab.services.RideRequestService;
import com.springboot.project.citycab.services.RiderService;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import com.springboot.project.citycab.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    // Repository
    private final RiderRepository riderRepository;
    // Service
    private final RideRequestService rideRequestService;
    // Strategy
    private final RideStrategyManager rideStrategyManager;
    // Mapper
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {

        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);
        rideRequest.setRider(rider);

        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

//        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        RideFareCalculationStrategy rideFareCalculationStrategy = rideStrategyManager.rideFareCalculationStrategy();
        Double fare = rideFareCalculationStrategy.calculateFare(rideRequest);
        rideRequest.setFare(fare);

        // For this we have to create the RideRequestService to save the rideRequest
        RideRequest savedRideRequest = rideRequestService.saveRideRequest(rideRequest);

        // Why do we use the rating of rider to find the driver?
        // broadcast the ride request to all drivers
        // find the matching driver
//        rideStrategyManager.driverMatchingStrategy().findMatchingDriver(rideRequest);

        // TODO: It should be the Driver rating should >= 4.8 rating
        DriverMatchingStrategy driverMatchingStrategy = rideStrategyManager
                .driverMatchingStrategy(rider.getRating()); // Here use the avg rating given to the driver by the rider
        List<Driver> drivers = driverMatchingStrategy.findMatchingDriver(rideRequest);

        // TODO: Send notification to all the drivers about this ride request


        // Here we save the ride request and
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

    @Transactional
    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    // Using Spring Security, we get the context of the current user and get the rider
    @Override
    public Rider getCurrentRider() {
        // TODO: implement Spring Security

        // currently we are returning with riderId = 1
        return riderRepository
                .findById(1L)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Rider not found with id: 1")
                );
    }
}