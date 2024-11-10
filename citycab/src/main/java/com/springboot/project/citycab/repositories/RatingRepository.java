package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Rating;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByRider(Rider rider);

    List<Rating> findAllByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);

    Page<Rating> findByDriver(Driver driver, Pageable pageable);

    Page<Rating> findByRider(Rider rider, Pageable pageable);
}
