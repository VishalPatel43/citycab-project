package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RatingDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Rating;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RatingService {

    DriverDTO rateDriver(Ride ride, RatingDTO ratingDTO);

    Rating createNewRating(Ride ride);

    Page<RatingDTO> getReviewsByRider(Long riderId, Pageable pageable);

    Page<RatingDTO> getReviewsForDriver(Long driverId, Pageable pageable);

    Rating getRatingByRide(Ride ride);

}
