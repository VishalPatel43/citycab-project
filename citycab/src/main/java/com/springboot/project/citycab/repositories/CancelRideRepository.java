package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.CancelRide;
import com.springboot.project.citycab.entities.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancelRideRepository extends JpaRepository<CancelRide, Long> {

    Page<CancelRide> findByCancelledBy(Role cancelledBy, Pageable pageable);

}
