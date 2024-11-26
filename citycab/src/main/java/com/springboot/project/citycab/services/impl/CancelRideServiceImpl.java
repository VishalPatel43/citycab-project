package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.constants.enums.RideStatus;
import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.CancelRideDTO;
import com.springboot.project.citycab.entities.CancelRide;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.repositories.CancelRideRepository;
import com.springboot.project.citycab.services.CancelRideService;
import com.springboot.project.citycab.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CancelRide cancelRide(Ride ride, String reason, Role cancelledBy) {

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new IllegalArgumentException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());

        // update the rideStatus to CANCELLED
        ride.setRideStatus(RideStatus.CANCELLED);
        ride.setFare(0.0);

        RideRequest rideRequest = ride.getRideRequest();
        rideRequest.setRideRequestStatus(RideRequestStatus.CANCELLED);

        ride.getRider().setAvailable(true);
        ride.getDriver().setAvailable(true);

        ride = rideService.saveRide(ride);

        // Create the CancelRide entity
        CancelRide cancelRide = CancelRide
                .builder()
                .cancelledAt(LocalDateTime.now())
                .reason(reason)
                .cancelledBy(cancelledBy)
                .ride(ride)
                .build();

        return cancelRideRepository.save(cancelRide);
    }

    @Override
    public Page<CancelRideDTO> getCancelRideByRole(Role cancelledBy, PageRequest pageRequest) {
        Page<CancelRide> cancelRides = cancelRideRepository.findByCancelledBy(cancelledBy, pageRequest);
        return cancelRides.map(this::mapToCancelRideDTO);
    }

    @Override
    public CancelRide getCancelRideByRideId(Long cancelRideId) {
        return cancelRideRepository.findById(cancelRideId)
                .orElseThrow(() -> new IllegalArgumentException("No CancelRide found for cancelRideId: " + cancelRideId));
    }

    @Override
    public CancelRideDTO getCancelRideById(Long cancelRideId) {
        CancelRide cancelRide = getCancelRideByRideId(cancelRideId);
        return mapToCancelRideDTO(cancelRide);
    }

    @Override
    public Page<CancelRideDTO> getAllCancelRides(PageRequest pageRequest) {
        Page<CancelRide> cancelRides = cancelRideRepository.findAll(pageRequest);
        return cancelRides.map(this::mapToCancelRideDTO);
    }

    @Override
    public CancelRideDTO updateCancelRide(Long cancelRideId, CancelRideDTO cancelRideDTO) {
        CancelRide cancelRide = getCancelRideByRideId(cancelRideId);

        cancelRide.setReason(cancelRideDTO.getReason());
        cancelRide.setCancelledBy(cancelRideDTO.getCancelledBy());

        CancelRide updatedCancelRide = cancelRideRepository.save(cancelRide);
        return mapToCancelRideDTO(updatedCancelRide);
    }

    private CancelRideDTO mapToCancelRideDTO(CancelRide cancelRide) {
        CancelRideDTO cancelRideDTO = modelMapper.map(cancelRide, CancelRideDTO.class);
        if (cancelRideDTO.getRide() != null && cancelRideDTO.getRide().getDriver() != null) {
            cancelRideDTO.getRide().getDriver().setVehicles(null); // Avoid circular references
        }
        return cancelRideDTO;
    }
}
