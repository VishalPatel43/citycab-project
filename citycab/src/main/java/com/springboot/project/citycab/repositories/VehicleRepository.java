package com.springboot.project.citycab.repositories;


import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//@RepositoryRestResource(exported = false) // This disables the entire repository from being exposed via REST
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByNumberPlate(String numberPlate);

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    @Query("SELECT v FROM Vehicle v JOIN v.drivers d WHERE d = :driver")
    List<Vehicle> findByDriver(Driver driver);
}
