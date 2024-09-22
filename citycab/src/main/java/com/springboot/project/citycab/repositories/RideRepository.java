package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageable);

    Page<Ride> findByDriver(Driver driver, Pageable pageable);
}
