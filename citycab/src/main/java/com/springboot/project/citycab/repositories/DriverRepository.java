package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Driver;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {


    // Methods in the PostgreSQL/PostGIS database
    // ST_Distance(point1, point2) returns the distance between two points
    // ST_DWithin(point1, 10000)


    // this query is SQL query not JPQL query
    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance " +
            "FROM drivers AS d " + // drivers is the table name
            "WHERE avaible = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
            "ORDER BY distance " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickupLocation);

    //    Pageable pageable = PageRequest.of(0, 10);
//    List<Driver> drivers = driverRepository.findTenNearestDrivers(pickupLocation, pageable);
//    @Query("SELECT d " +
//            "FROM Driver d " +
//            "WHERE d.available = true AND ST_DWithin(d.currentLocation, :pickupLocation, 10000) " +
//            "ORDER BY ST_Distance(d.currentLocation, :pickupLocation) ASC")
//    List<Driver> findTenNearestDrivers(Point pickupLocation, Pageable pageable);
}
