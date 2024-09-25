package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.*;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.entities.enums.RideStatus;
import com.springboot.project.citycab.entities.enums.Role;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.RiderRepository;
import com.springboot.project.citycab.services.*;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import com.springboot.project.citycab.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    // Repository
    private final RiderRepository riderRepository;
    // Service
    private final RideRequestService rideRequestService;
    private final RideService rideService;
    private final DriverService driverService;
    private final CancelRideService cancelRideService;
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

        // TODO: It should be the Driver rating should >= 4.8 rating
        DriverMatchingStrategy driverMatchingStrategy = rideStrategyManager
                .driverMatchingStrategy(rider.getRating()); // Here use the avg rating given to the driver by the rider
        List<Driver> drivers = driverMatchingStrategy.findMatchingDriver(rideRequest);

        // TODO: Send notification to all the drivers about this ride request

        // Here we save the ride request and
        return modelMapper.map(savedRideRequest, RideRequestDTO.class);
    }

    @Override
    @Transactional
    public RideDTO cancelRide(Long rideId, String reason) {

        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if (!rider.equals(ride.getRider()))
            throw new RuntimeException(("Rider does not own this ride with id: " + rideId));

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());

        Ride savedRide = cancelRideService.cancelRide(
                ride,
                reason,
                Role.RIDER
        );

        driverService.updateDriverAvailability(ride.getDriver(), true);

        return modelMapper.map(savedRide, RideDTO.class);
    }

    @Override
    @Transactional
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDTO getMyProfile() {
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider, RiderDTO.class);
    }

    // All the rides of the rider
    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService
                .getAllRidesOfRider(currentRider, pageRequest)
                .map(ride -> modelMapper.map(ride, RideDTO.class));
    }

    @Override
    @Transactional
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public OtpDTO getOtp(Long rideId) {
        return rideService.getOtp(rideId);
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

    @Override
    public Page<CancelRideDTO> getCancelledRidesByRider(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();

        if (currentRider == null)
            throw new ResourceNotFoundException("Rider not found");
        // Fetch cancelled rides for the current driver
        Page<CancelRide> cancelledRides = cancelRideService.getCancelRideByRole(Role.RIDER, pageRequest);

        return cancelledRides.map(cancelRide -> modelMapper.map(cancelRide, CancelRideDTO.class));
    }
}