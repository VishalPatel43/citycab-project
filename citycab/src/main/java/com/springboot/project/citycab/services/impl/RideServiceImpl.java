package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.RideRequest;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
import com.springboot.project.citycab.entities.enums.RideStatus;
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
        rideRequestService.updateRideRequest(rideRequest);

        // Here we convert RideRequest to Ride for some of the fields which has same name
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
//        ride.setRideId(null); // not required coz we already have different rideId and rideRequestId

        return rideRepository.save(ride);
    }

    @Override
    @Transactional
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        rideRepository.findById(ride.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + ride.getRideId()));
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
    @Transactional
    public Ride updateRide(Ride ride) {
        rideRepository.findById(ride.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + ride.getRideId()));
        return rideRepository.save(ride);
    }

    private String generateRandomOTP() {
        Random random = new Random();

        // but not generate 0001 or 0011 or 0111
//        int otp = 1000 + random.nextInt(9000);  // Ensures OTP is between 1000 and 9999
//        return String.valueOf(otp);  // No need for String.format since it's already a 4-digit number

        // it will generate the 0000 also but don't want all zero so we use + 1
//        int otp = random.nextInt(10000);  //0 to 9999
        int otp = 1 + random.nextInt(9999);  // Generates a number between 1 and 9999
        return String.format("%04d", otp);   // Zero-pads the number to 4 digits

    }
}
