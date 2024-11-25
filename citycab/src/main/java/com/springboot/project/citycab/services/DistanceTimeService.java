package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DistanceTimeResponseDTO;
import org.locationtech.jts.geom.Point;

// Use the Functional Interface --> We can use as lambda expression
public interface DistanceTimeService {

     DistanceTimeResponseDTO calculateDistanceTime(Point src, Point dest);

}
