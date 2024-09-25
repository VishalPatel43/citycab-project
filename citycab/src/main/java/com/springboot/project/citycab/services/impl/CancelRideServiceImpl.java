package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.CancelRide;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.enums.RideStatus;
import com.springboot.project.citycab.entities.enums.Role;
import com.springboot.project.citycab.repositories.CancelRideRepository;
import com.springboot.project.citycab.services.CancelRideService;
import com.springboot.project.citycab.services.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CancelRideServiceImpl implements CancelRideService {

    private final CancelRideRepository cancelRideRepository;
    private final RideService rideService;

    @Override
    @Transactional
    public Ride cancelRide(Ride ride, String reason, Role cancelledBy) {

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());

        // update the rideStatus to CANCELLED
        ride.setRideStatus(RideStatus.CANCELLED);
        ride.setFare(0.0);

        // Create the CancelRide entity
        CancelRide cancelRide = CancelRide
                .builder()
                .cancelledAt(LocalDateTime.now())
                .reason(reason)
                .cancelledBy(cancelledBy)
                .ride(ride)
                .build();

        CancelRide updateCancelRide = cancelRideRepository.save(cancelRide);
        ride.setCancelRide(updateCancelRide);

        return rideService.updateRide(ride);
    }

    @Override
    public Page<CancelRide> getCancelRideByRole(Role cancelledBy, PageRequest pageRequest) {
        // Fetch cancelled rides for the current driver
        return cancelRideRepository.findByCancelledBy(cancelledBy, pageRequest);
    }
}