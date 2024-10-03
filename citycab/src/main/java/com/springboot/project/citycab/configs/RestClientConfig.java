package com.springboot.project.citycab.configs;

import com.springboot.project.citycab.exceptions.DistanceRestClientServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestClientConfig {

    @Bean
    @Primary
    @Qualifier("distanceServiceOSRM")
    public RestClient getDistanceServiceOSRMRestClient() {
        // Here we handle the 5xx Server Error coz server is common for all the clients
        String OSRM_API_URL = "https://router.project-osrm.org/route/v1/driving/";
        return RestClient
                .builder()
                .baseUrl(OSRM_API_URL)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
                    String error = new String(res.getBody().readAllBytes());
                    throw new DistanceRestClientServiceException("Server Error: OSRM API is unavailable. Status Code: "
                            + res.getStatusCode() + ", Message: " + error);
                })
                .build();
    }

    @Bean
    @Qualifier("hereRoutingRestClient")
    public RestClient getHereRoutingRestClient() {
        String HERE_ROUTING_API_BASE_URL = "https://api.geoapify.com/v1/routing";
        return RestClient
                .builder()
                .baseUrl(HERE_ROUTING_API_BASE_URL)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
                    String error = new String(res.getBody().readAllBytes());
                    throw new DistanceRestClientServiceException("Server Error: Here Routing API is unavailable. Status Code: "
                            + res.getStatusCode() + ", Message: " + error);
                })
                .build();
    }

    @Bean
    @Qualifier("otherRestClient")
    public RestClient getRoutingRestClient() {
        String HERE_ROUTING_API_BASE_URL = "https://api.geoapify.com/v1/routing/";
        return RestClient
                .builder()
                .baseUrl(HERE_ROUTING_API_BASE_URL)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
                    String error = new String(res.getBody().readAllBytes());
                    throw new DistanceRestClientServiceException("Server Error: Here Routing API is unavailable. Status Code: "
                            + res.getStatusCode() + ", Message: " + error);
                })
                .build();
    }
}
