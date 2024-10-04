package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
//@RepositoryRestResource(exported = false)
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
}
