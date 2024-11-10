package com.springboot.project.citycab.constants;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

/**
 * A utility class holding constants related to CORS configurations.
 */
public final class CorsConstants {

    // Prevent instantiation
    private CorsConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "http://localhost:4200",
//            "http://localhost:8080", // for swagger-ui
            "https://citycab.herokuapp.com"
    );

    public static final List<String> ALLOWED_HEADERS = List.of(
            AUTHORIZATION,
            CONTENT_TYPE,
            ACCEPT
    );

    public static final List<String> ALLOWED_METHODS = List.of(
            GET.name(),
            POST.name(),
            PUT.name(),
            DELETE.name()
    );

    public static final List<String> EXPOSED_HEADERS = List.of(
            AUTHORIZATION
    );

    public static final Long MAX_AGE = 3600L; // 1 hour

    public static final int CORS_FILTER_ORDER = -102; // CORS filter must run before Spring Security
}
