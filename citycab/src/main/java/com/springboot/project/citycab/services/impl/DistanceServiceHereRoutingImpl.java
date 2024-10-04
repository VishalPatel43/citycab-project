package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.exceptions.DistanceRestClientServiceException;
import com.springboot.project.citycab.services.DistanceService;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class DistanceServiceHereRoutingImpl implements DistanceService {

    private final RestClient hereRoutingRestClient;

    // Manually define the constructor with @Qualifier
    public DistanceServiceHereRoutingImpl(@Qualifier("hereRoutingRestClient") RestClient hereRoutingRestClient) {
        this.hereRoutingRestClient = hereRoutingRestClient;
    }

    @Value("${here.routing.api.key}")
    private String hereRoutingApiKey; // Inject API key from properties
    /*
        https://www.geoapify.com/
        https://api.geoapify.com/v1/routing?waypoints=
        https://api.geoapify.com/v1/routing?waypoints=50.96209827745463%2C4.414458883409225%7C50.429137079078345%2C5.00088081232559&mode=drive&apiKey=6ab844d8f75042b8a88353ede5a3739b
     */

    @Override
    public double calculateDistance(Point src, Point dest) {

        try {
            String uri = "?waypoints=" + src.getY() + "," + src.getX() + "|" + dest.getY() + "," + dest.getX() + "&mode=drive&apiKey=" + hereRoutingApiKey;
//            String uri = String.format("?waypoints=%f,%f|%f,%f&mode=drive&apiKey=%s",
//                    src.getY(), src.getX(),
//                    dest.getY(), dest.getX(),
//                    hereRoutingApiKey);
            HereRoutingResponseDTO responseDto = hereRoutingRestClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        String error = new String(res.getBody().readAllBytes());
                        System.out.println("Client Error occurred: " + error);
                        throw new DistanceRestClientServiceException("Client Error: Unable to calculate distance. Here Routing responded with: " + error);
                    })
                    .body(HereRoutingResponseDTO.class);

            if (responseDto == null || responseDto.getFeatures().isEmpty())
                throw new DistanceRestClientServiceException("Error: No valid route found between the provided points.");

            return responseDto.getFeatures().getFirst().getProperties().getDistance() / 1000.0; // Convert meters to kilometers
        } catch (Exception e) {
            throw new DistanceRestClientServiceException("Error getting data from Here Routing: " + e.getMessage(), e);
        }
    }
}

// DTOs

@Data
@ToString
class HereRoutingResponseDTO {
    private List<HereRoutingRoute> features;
}

@Data
class HereRoutingRoute {
    private HereRoutingProperties properties;
}

@Data
class HereRoutingProperties {
    private Long distance; // Distance in meters
}
