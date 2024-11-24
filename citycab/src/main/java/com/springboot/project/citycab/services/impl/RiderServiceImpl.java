package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.constants.enums.RideStatus;
import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.*;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.RiderRepository;
import com.springboot.project.citycab.services.*;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import com.springboot.project.citycab.strategies.RideDistanceTimeFareCalculationStrategy;
import com.springboot.project.citycab.strategies.manager.DistanceTimeServiceManager;
import com.springboot.project.citycab.strategies.manager.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final UserService userService;
    private final RideRequestService rideRequestService;
    private final RideService rideService;
    private final DriverService driverService;
    private final CancelRideService cancelRideService;
    private RatingService ratingService;
    // Strategy
    private final RideStrategyManager rideStrategyManager;
    private final DistanceTimeServiceManager distanceTimeServiceManager;
    // Mapper
    private final ModelMapper modelMapper;

    @Autowired
    public void setRatingService(@Lazy RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {

        Rider rider = getCurrentRider();

        if (!rider.getAvailable())
            throw new RuntimeException("Rider is not available to request a ride");

        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);
        rideRequest.setRider(rider);

        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

        RideDistanceTimeFareCalculationStrategy rideDistanceTimeFareCalculationStrategy = rideStrategyManager.rideFareCalculationStrategy();
        DistanceTimeFareDTO distanceTimeFare = rideDistanceTimeFareCalculationStrategy.calculateDistanceTimeFare(rideRequest);

        rideRequest.setRideDistance(distanceTimeFare.getDistance());
        rideRequest.setRideTime(distanceTimeFare.getTime());
        rideRequest.setFare(distanceTimeFare.getFare());

        // For this we have to create the RideRequestService to save the rideRequest
        RideRequest savedRideRequest = rideRequestService.saveRideRequest(rideRequest);

        DriverMatchingStrategy driverMatchingStrategy = rideStrategyManager
                .driverMatchingStrategy();

        List<Driver> drivers = driverMatchingStrategy.findMatchingDriver(savedRideRequest);

        rideRequest.setDrivers(drivers);
        rideRequest.getRider().setAvailable(false);
        savedRideRequest = rideRequestService.saveRideRequest(rideRequest);

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

        CancelRide cancelRide = cancelRideService.cancelRide(
                ride,
                reason,
                Role.RIDER
        );

        driverService.confirmAndClearAssociations(ride.getRideRequest());
        driverService.updateDriverAvailability(ride.getDriver(), true);

        RideDTO rideDTO = modelMapper.map(cancelRide.getRide(), RideDTO.class);

        CancelRideDTO cancelRideDTO = modelMapper.map(cancelRide, CancelRideDTO.class);
        cancelRideDTO.setRide(null);
        rideDTO.setCancelRide(cancelRideDTO);
        rideDTO.setRating(null);
        return rideDTO;
    }

    @Override
    @Transactional
    public DriverDTO submitRating(Long rideId, RatingDTO ratingDTO) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if (!rider.equals(ride.getRider()))
            throw new RuntimeException("Rider is not the owner of this Ride");

        if (!ride.getRideStatus().equals(RideStatus.ENDED))
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: " + ride.getRideStatus());

        return ratingService.rateDriver(ride, ratingDTO);
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
    public Page<RatingDTO> getReviewsByRider(PageRequest pageRequest) {

        Rider currentRider = getCurrentRider();
        if (currentRider == null)
            throw new ResourceNotFoundException("Rider not found");

        return ratingService.getReviewsByRider(currentRider, pageRequest);
    }

    @Override
    @Transactional
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .avgGivenRating(0.0)
                .available(true)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider updateRider(Rider rider) {
        return riderRepository.save(rider);
    }

    @Override
    public OtpDTO getOtp(Long rideId) {
        return rideService.getOtp(rideId);
    }

    // Using Spring Security, we get the context of the current user and get the rider
    @Override
    public Rider getCurrentRider() {
        User currentUser = userService.getCurrentUser();

        // currently we are returning with riderId = 1
        return riderRepository
                .findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rider not associated with the current user id: " +
                                currentUser.getUserId())
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

    @Override
    public Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest) {

        Page<Rider> riders = riderRepository
                .findByUserNameContainingIgnoreCase(name, pageRequest);
        // If no riders are found, throw a custom exception
        if (riders.isEmpty())
            throw new ResourceNotFoundException("No riders found with name: " + name);

        return riders.map(rider -> modelMapper.map(rider, RiderDTO.class));
    }

    @Override
    public List<DriverDTO> getAvailableDriversForRideRequest(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.getRideRequestById(rideRequestId);
        List<Driver> drivers = rideRequest.getDrivers();

        return drivers.stream()
                .map(driver -> {
                    DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
                    driverDTO.setVehicles(null);
                    return driverDTO;
                })
                .toList();
    }

    @Transactional
    @Override
    public RideRequestDTO cancelRideRequestByRider(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.getRideRequestById(rideRequestId);

        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING))
            throw new RuntimeException("RideRequest cannot be cancelled, status is " + rideRequest.getRideRequestStatus());

        rideRequest.setRideRequestStatus(RideRequestStatus.CANCELLED);
        rideRequest.getRider().setAvailable(true);

        return driverService.confirmAndClearAssociations(rideRequest);
    }

    @Transactional
    @Override
    public DistanceTimeResponseDTO driverToRiderDistanceTime(Long rideId, PointDTO driverLocation) {
        Rider rider = getCurrentRider();

        Ride ride = rideService.getRideById(rideId);
//        Driver driver = ride.getDriver();

        if (!ride.getRider().getRiderId().equals(rider.getRiderId()))
            throw new RuntimeException("Rider is not the owner of this Ride");

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride status is not Confirmed hence cannot start the ride, status: " + ride.getRideStatus());

        Point currentLocation = modelMapper.map(driverLocation, Point.class);
        DistanceTimeResponseDTO responseDTO = calculateTimeDistance(currentLocation, ride.getRideRequest().getPickupLocation());

        log.info("Driver to Rider Remaining: Distance={} km, Time={} min", responseDTO.getDistanceKm(), responseDTO.getTimeMinutes());
        return responseDTO;
    }

    @Transactional
    @Override
    public DistanceTimeResponseDTO riderToDestinationDistanceTime(Long rideId, PointDTO driverLocation) {
        Rider rider = getCurrentRider();

        Ride ride = rideService.getRideById(rideId);

        if (!ride.getRider().getRiderId().equals(rider.getRiderId()))
            throw new RuntimeException("Rider is not the owner of this Ride");

        if (!ride.getRideStatus().equals(RideStatus.ONGOING))
            throw new RuntimeException("Ride status is not Started hence cannot calculate the distance and time to the destination, status: " + ride.getRideStatus());

        Point currentLocation = modelMapper.map(driverLocation, Point.class);
        DistanceTimeResponseDTO responseDTO = calculateTimeDistance(currentLocation, ride.getDropOffLocation());

        log.info("Rider to Destination Remaining: Distance={} km, Time={} min", responseDTO.getDistanceKm(), responseDTO.getTimeMinutes());
        return responseDTO;
    }

    @Transactional
    public DistanceTimeResponseDTO calculateTimeDistance(Point source, Point destination) {

        return distanceTimeServiceManager
                .calculateDistanceTime(source, destination);
    }
}
