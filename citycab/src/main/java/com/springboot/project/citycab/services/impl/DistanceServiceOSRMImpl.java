package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.exceptions.OSRMServiceException;
import com.springboot.project.citycab.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    // TODO: Implement the method to calculate the distance between two points
    // Call third party service/API OSRM to calculate the distance

    // Link: https://router.project-osrm.org/route/v1/driving/13.388860,52.517037;13.397634,52.529407;13.428555,52.523219?overview=false
    // source, destination, waypoint (calculate with other points) -> latitude,longitude -> 13.388860,52.517037;13.397634,52.529407;13.428555,52.523219

    // but we need only source and destination (only 1 point)
    private static final String OSRM_API_URL = "https://router.project-osrm.org/route/v1/driving/";

    @Override
    public double calculateDistance(Point src, Point dest) {

        // We use RestClient only for this method so don't need to create a separate config for this
        // getX and getY are the latitude and longitude
        try {
            String uri = src.getX() + "," + src.getY() + ";" + dest.getX() + "," + dest.getY();
            OSRMResponseDTO osrmResponseDTO = RestClient.builder()
                    .baseUrl(OSRM_API_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDTO.class);

            if (osrmResponseDTO == null)
                throw new OSRMServiceException("Error getting data from OSRM: Response was null");

            // return shortest distance in km
            //  return osrmResponseDTO.getRoutes().get(0).getDistance() / 1000.0;
            return osrmResponseDTO.getRoutes().getFirst().getDistance() / 1000.0; // get first distance from the list
        } catch (Exception e) {
            throw new RuntimeException("Error getting data from OSRM " + e.getMessage());
        }
    }
}


// OSRM API give array of response with many details, we take only distance which is a list of distance
// We take only first distance from the list
@Data
class OSRMResponseDTO {
    private List<OSRMRoutes> routes; // Routes names also same in the OSRM API response so Jackson will map it automatically
}

// But we want only distance so we need to create a DTO class to map the response
@Data
class OSRMRoutes {
    private Double distance; // distance name also same in the OSRM API response so Jackson will map it automatically
}
