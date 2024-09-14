package com.springboot.project.citycab.strategies;

import com.springboot.project.citycab.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.springboot.project.citycab.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.springboot.project.citycab.strategies.impl.RideFareSurgePricingRideFareCalculationStrategy;
import com.springboot.project.citycab.strategies.impl.RiderFareDefaultRideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;

    private final RiderFareDefaultRideFareCalculationStrategy defaultRideFareCalculationStrategy;
    private final RideFareSurgePricingRideFareCalculationStrategy surgePricingRideFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating) {
        return (riderRating >= 4.8) ? highestRatedDriverStrategy : nearestDriverStrategy;
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {

        // Only for pick hour
        // 6PM to 9PM is SURGE TIME

        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        return isSurgeTime ? surgePricingRideFareCalculationStrategy : defaultRideFareCalculationStrategy;
    }
}
