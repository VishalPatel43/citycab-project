package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//@RepositoryRestResource(exported = false) // This disables the entire repository from being exposed via REST
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // Find Driver by user's name
    Page<Driver> findByUserNameContainingIgnoreCase(String name, Pageable pageable);

    /*

     Methods in the PostgreSQL/PostGIS database
     ST_Distance(point1, point2) returns the distance between two points
     ST_DWithin(point1, 10000)
    */


    /**
     * Primary Condition:
     * Find available drivers within 0-2 km with avg_rating >= 4.5.
     * Sorted by avg_rating DESC and distance ASC.
     */
    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS driverDistanceFromRider " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 2000) " +
            "AND d.avg_rating >= 4.5 " +
            "ORDER BY d.avg_rating DESC, driverDistanceFromRider ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTopRatedDriversWithin2Km(Point pickupLocation);

    /**
     * Secondary Condition:
     * Find available drivers within 0-3 km, sorted by avg_rating DESC and distance ASC.
     */
    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS driverDistanceFromRider " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 3000) " +
            "ORDER BY d.avg_rating DESC, driverDistanceFromRider ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findHighestRatedDriversWithin3Km(Point pickupLocation);

    /**
     * Tertiary Condition:
     * Find available drivers within 3-10 km with avg_rating < 4,
     * sorted by distance ASC and avg_rating DESC.
     */
    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS driverDistanceFromRider " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
            "AND NOT ST_DWithin(d.current_location, :pickupLocation, 3000) " +
            "AND d.avg_rating < 4 " +
            "ORDER BY driverDistanceFromRider ASC, d.avg_rating DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findNearestDriversFrom3To10KmWithLowRating(Point pickupLocation);

    //    @RestResource(exported = false)
    // this query is SQL query not JPQL query
    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS driverDistanceFromRider " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
            "ORDER BY driverDistanceFromRider ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickupLocation);

  /*

    Pageable pageable = PageRequest.of(0, 10);
    List<Driver> drivers = driverRepository.findTenNearestDrivers(pickupLocation, pageable);
    @Query("SELECT d " +
            "FROM Driver d " +
            "WHERE d.available = true AND ST_DWithin(d.currentLocation, :pickupLocation, 10000) " +
            "ORDER BY ST_Distance(d.currentLocation, :pickupLocation) ASC")
    List<Driver> findTenNearestDrivers(Point pickupLocation, Pageable pageable);
   */

    @Query(value = "SELECT d.* " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 15000) " +
            "ORDER BY d.avg_rating DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    Optional<Driver> findByUser(User currentUser);

    Optional<Driver> findByDrivingLicenseNumber(String drivingLicenseNumber);

    Optional<Driver> findByAadharCardNumber(Long aadharCardNumber);
}
