package com.springboot.project.citycab.strategies.manager;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.strategies.DriverMatchingStrategy;
import com.springboot.project.citycab.strategies.RideFareCalculationStrategy;
import com.springboot.project.citycab.strategies.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedStrategy;
    private final DriverMatchingNearestDriverStrategy nearestStrategy;
    private final DriverMatchingAdvancedStrategy advancedStrategy;

    private final RiderFareDefaultRideFareCalculationStrategy defaultRideFareCalculationStrategy;
    private final RideFareSurgePricingRideFareCalculationStrategy surgePricingRideFareCalculationStrategy;

    /**
     * Returns a DriverMatchingStrategy that encapsulates the three conditions:
     * 1. Primary: 0-2 km, avg_rating >= 4.5
     * 2. Secondary: 0-3 km, sorted by rating and distance
     * 3. Tertiary: 3-10 km, avg_rating <4, sorted by distance and rating
     */

    public DriverMatchingStrategy driverMatchingStrategy() {
        return rideRequest -> {
            // 1. Primary Condition: 0-2 km with avg_rating >=4.5
            List<Driver> driversPrimary = highestRatedStrategy.findMatchingDriver(rideRequest);
            if (!driversPrimary.isEmpty())
                return driversPrimary;

            // 2. Secondary Condition: 0-3 km sorted by rating and distance
            List<Driver> driversSecondary = advancedStrategy.findMatchingDriver(rideRequest);
            if (!driversSecondary.isEmpty())
                return driversSecondary;

            // 3. Tertiary Condition: 3-10 km with avg_rating < 4, sorted by distance and rating
//                List<Driver> driversTertiary = nearestStrategy.findMatchingDriver(rideRequest); //                return driversTertiary;
            return nearestStrategy.findMatchingDriver(rideRequest);
        };
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {

        /*
         *  Only for pick hour
         * 6PM to 9PM is SURGE TIME
         **/
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        return isSurgeTime ? surgePricingRideFareCalculationStrategy : defaultRideFareCalculationStrategy;
    }
}
