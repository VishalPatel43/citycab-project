package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    // TODO: Implement the method to calculate the distance between two points
    // Call third party service/API OSRM to calculate the distance

    @Override
    public double calculateDistance(Point src, Point dest) {
        return 0;
    }
}
