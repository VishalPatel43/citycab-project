package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DistanceTimeResponseDTO;
import org.locationtech.jts.geom.Point;

public interface DistanceTimeService {

     DistanceTimeResponseDTO calculateDistanceTime(Point src, Point dest);
}
