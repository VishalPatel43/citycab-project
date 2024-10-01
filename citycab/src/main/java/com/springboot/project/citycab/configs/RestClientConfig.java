package com.springboot.project.citycab.configs;

import com.springboot.project.citycab.exceptions.OSRMServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestClientConfig {

    @Bean
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
                    throw new RuntimeException("Server Error: OSRM API is unavailable. Status Code: "
                            + res.getStatusCode() + ", Message: " + error);
                })
                .build();
    }
}
