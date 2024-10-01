package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    // Find rider by user's name
    Page<Rider> findByUserNameContainingIgnoreCase(String name, Pageable pageable);
}
