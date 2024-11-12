package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.constants.enums.RideStatus;
import com.springboot.project.citycab.dto.OtpDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.RideRepository;
import com.springboot.project.citycab.services.RideRequestService;
import com.springboot.project.citycab.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    // Repository
    private final RideRepository rideRepository;
    // Service
    private final RideRequestService rideRequestService;
    // Mapper
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));
    }

    @Override
    @Transactional
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        rideRequestService.saveRideRequest(rideRequest);

        // Here we convert RideRequest to Ride for some of the fields which has same name
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideRequest(rideRequest);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
//        ride.setRideId(null); // not required coz we already have different rideId and rideRequestId

        return rideRepository.save(ride);
    }

    @Override
    @Transactional
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        getRideById(ride.getRideId());

        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver, pageRequest);
    }

    @Override
    public OtpDTO getOtp(Long rideId) {
        Ride ride = getRideById(rideId);

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride is not in CONFIRMED status, OTP cannot be generated");
        if (ride.getOtp() == null)
            throw new RuntimeConflictException("OTP not generated for this ride.");
        return new OtpDTO(ride.getOtp());
    }

    @Override
    @Transactional
    public Ride updateRide(Ride ride) {
        getRideById(ride.getRideId());
        return rideRepository.save(ride);
    }

    private String generateRandomOTP() {
        Random random = new Random();
        int otp = 1 + random.nextInt(9999);  // Generates a number between 1 and 9999
        return String.format("%04d", otp);   // Zero-pads the number to 4 digits
    }
}
