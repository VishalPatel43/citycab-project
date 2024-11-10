package com.springboot.project.citycab.configs;

import com.springboot.project.citycab.constants.RepositoryConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {

        // Retrieve unsupported HTTP methods from constants
        HttpMethod[] theUnsupportedActions = RepositoryConstants.UNSUPPORTED_HTTP_METHODS.toArray(new HttpMethod[0]);

        // Set custom base path using constants
        config.setBasePath(RepositoryConstants.BASE_PATH);

        // Array of all entity classes from constants
        Class<?>[] entityClasses = RepositoryConstants.EXPOSED_ENTITY_CLASSES;

        // Expose entity IDs
        config.exposeIdsFor(entityClasses);

        // Disable HTTP methods for all entity classes using constants
        for (Class<?> entityClass : entityClasses) {
            disableHttpMethods(entityClass, config, theUnsupportedActions);
        }
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
