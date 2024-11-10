package com.springboot.project.citycab.constants;

import com.springboot.project.citycab.entities.*;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

/**
 * A utility class holding constants related to repository configurations.
 */
public final class RepositoryConstants {

    // Prevent instantiation
    private RepositoryConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * The base path for all REST repository endpoints.
     */
    public static final String BASE_PATH = "/restrepo";

    /**
     * The list of HTTP methods that are unsupported and should be disabled for repositories.
     */
    public static final List<HttpMethod> UNSUPPORTED_HTTP_METHODS = List.of(
            POST,
            PATCH,
            DELETE,
            PUT
    );

    /**
     * The array of entity classes for which IDs should be exposed and unsupported HTTP methods disabled.
     */
    public static final Class<?>[] EXPOSED_ENTITY_CLASSES = {
            Driver.class,
            Ride.class,
            RideRequest.class,
            Rider.class,
            User.class,
            Wallet.class,
            WalletTransaction.class,
            Payment.class
    };
}
