package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RatingDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Rating;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.RatingRepository;
import com.springboot.project.citycab.services.DriverService;
import com.springboot.project.citycab.services.RatingService;
import com.springboot.project.citycab.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    // Service
    private final RiderService riderService;
    private DriverService driverService;
    // Mapper
    private final ModelMapper modelMapper;

    @Autowired
    public void setDriverService(@Lazy DriverService driverService) { // no nood for 1
        this.driverService = driverService;
    }

    @Override
    @Transactional
    public DriverDTO rateDriver(Ride ride, RatingDTO ratingDTO) {

        Driver driver = ride.getDriver();
        Rider rider = ride.getRider();

        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getRideId()));

        if (ratingObj.getDriverRating() != null)
            throw new RuntimeConflictException("Driver has already been rated, cannot rate again");

        // Convert Double to Integer
        ratingObj.setDriverRating(ratingDTO.getDriverRating().intValue());
        ratingObj.setComment(ratingDTO.getComment());
        ratingObj.setRatingDate(LocalDateTime.now());

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findAllByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);
        driver.setAvgRating(newRating);

        Driver savedDriver = driverService.saveDriver(driver);
//        Driver savedDriver = driverRatingService.updateDriverRating(driver, newRating);


        // Update rider's average given rating
        Rider saveRider = updateRiderRating(rider);

        // Save the updated rider with the new average rating
        riderService.updateRider(rider);  // Ensure `save` method is available in `RiderService`

        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    @Transactional
    public Rating createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        return ratingRepository.save(rating);
    }

    @Override
    public Page<RatingDTO> getReviewsByRider(Rider rider, Pageable pageable) {

        Page<Rating> ratings = ratingRepository.findByRider(rider, pageable);

        if (ratings.isEmpty())
            throw new ResourceNotFoundException("No ratings found for rider with id: " + rider.getRiderId());

        return ratings.map(rating -> modelMapper.map(rating, RatingDTO.class));
    }

    @Override
    public Page<RatingDTO> getReviewsForDriver(Driver driver, Pageable pageable) {

        Page<Rating> ratings = ratingRepository.findByDriver(driver, pageable);

        if (ratings.isEmpty())
            throw new ResourceNotFoundException("No ratings found for driver with id: " + driver.getDriverId());

        return ratings.map(rating -> modelMapper.map(rating, RatingDTO.class));
    }

    @Override
    public Rating getRatingByRide(Ride ride) {
        return ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getRideId()));
    }

    private Driver updateDriverRating(Driver driver) {
        double newRating = ratingRepository.findAllByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);

        driver.setAvgRating(newRating);
        return driverService.saveDriver(driver);
    }

    private Rider updateRiderRating(Rider rider) {
        double newRiderGivenRating = ratingRepository.findAllByRider(rider)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);

        rider.setAvgGivenRating(newRiderGivenRating);
        return riderService.updateRider(rider);
    }
}
