package com.springboot.project.citycab.configs;

import com.springboot.project.citycab.entities.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private static final String theAllowedOrigins = "http://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {
//                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT
        };

        // Array of all entity classes to expose IDs for and disable HTTP methods
        Class<?>[] entityClasses = {
                Driver.class,
                Ride.class,
                RideRequest.class,
                Rider.class,
                User.class
        };

        // Expose entity IDs
        config.exposeIdsFor(entityClasses);

        // Disable HTTP methods for all entity classes in the array
        for (Class<?> entityClass : entityClasses)
            disableHttpMethods(entityClass, config, theUnsupportedActions);


        /* Configure CORS Mapping */
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }

    private void disableHttpMethods(Class<?> theClass,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metadata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions));
    }
}
