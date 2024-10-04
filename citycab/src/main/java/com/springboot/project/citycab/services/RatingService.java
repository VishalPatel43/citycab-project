package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RatingDTO;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Rating;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RatingService {

    DriverDTO rateDriver(Ride ride, RatingDTO ratingDTO);

    Rating createNewRating(Ride ride);

    Page<RatingDTO> getReviewsByRider(Rider rider, Pageable pageable);

    Page<RatingDTO> getReviewsForDriver(Driver driver, Pageable pageable);

    Rating getRatingByRide(Ride ride);

}
