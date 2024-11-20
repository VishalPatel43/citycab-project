package com.springboot.project.citycab.services.impl.distancetime;

import com.springboot.project.citycab.dto.DistanceTimeResponseDTO;
import com.springboot.project.citycab.exceptions.DistanceRestClientServiceException;
import com.springboot.project.citycab.services.DistanceTimeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class DistanceTimeTimeServiceHereRoutingImpl implements DistanceTimeService {

    private final RestClient hereRoutingRestClient;

    // Manually define the constructor with @Qualifier
    public DistanceTimeTimeServiceHereRoutingImpl(@Qualifier("hereRoutingRestClient") RestClient hereRoutingRestClient) {
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
    public DistanceTimeResponseDTO calculateDistanceTime(Point src, Point dest) {

        try {
            String uri = "?waypoints=" + src.getY() + "," + src.getX() + "|" + dest.getY() + "," + dest.getX() + "&mode=drive&apiKey=" + hereRoutingApiKey;
//            String uri = String.format("?waypoints=%f,%f|%f,%f&mode=drive&apiKey=%s",
//                    src.getY(), src.getX(),
//                    dest.getY(), dest.getX(),
//                    hereRoutingApiKey);
            HereRoutingResponseDTO responseDTO = hereRoutingRestClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        String error = new String(res.getBody().readAllBytes());
                        System.out.println("Client Error occurred: " + error);
                        throw new DistanceRestClientServiceException("Client Error: Unable to calculate distance. Here Routing responded with: " + error);
                    })
                    .body(HereRoutingResponseDTO.class);

            if (responseDTO == null || responseDTO.getFeatures().isEmpty())
                throw new DistanceRestClientServiceException("Error: No valid route found between the provided points.");

            // Extract distance and time from the first feature
            HereRoutingProperties properties = responseDTO.getFeatures().getFirst().getProperties();
            double distanceKm = properties.getDistance() / 1000.0; // Convert meters to kilometers
            double timeMinutes = properties.getTime() / 60.0;     // Convert seconds to minutes

            // Return as a structured response
            return new DistanceTimeResponseDTO(distanceKm, timeMinutes);

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

@AllArgsConstructor
@NoArgsConstructor
@Data
class HereRoutingProperties {
    private Long distance; // Distance in meters
    private Double time;
}
