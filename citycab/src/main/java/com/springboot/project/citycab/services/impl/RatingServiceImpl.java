package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RatingDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Rating;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.DriverRepository;
import com.springboot.project.citycab.repositories.RatingRepository;
import com.springboot.project.citycab.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public DriverDTO rateDriver(Ride ride, RatingDTO ratingDTO) {

        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getRideId()));

        if (ratingObj.getDriverRating() != null)
            throw new RuntimeConflictException("Driver has already been rated, cannot rate again");

        // Convert Double to Integer
        ratingObj.setDriverRating(ratingDTO.getDriverRating().intValue());
        ratingObj.setComment(ratingDTO.getComment());
        ratingObj.setRatingDate(LocalDateTime.now());

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);
        driver.setAvgRating(newRating);

        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public Rating createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        return ratingRepository.save(rating);
    }

    @Override
    public Page<RatingDTO> getReviewsByRider(Long riderId, Pageable pageable) {

        return null;
    }

    @Override
    public Page<RatingDTO> getReviewsForDriver(Long driverId, Pageable pageable) {
        return null;
    }

    @Override
    public Rating getRatingByRide(Ride ride) {
        return ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getRideId()));
    }
}
