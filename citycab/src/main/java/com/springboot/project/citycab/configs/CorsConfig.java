package com.springboot.project.citycab.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.springboot.project.citycab.constants.CorsConstants.*;

// Cross-Origin Resource Sharing (CORS) configuration
// add -> .cors(AbstractHttpConfigurer::disable) // Disable CORS at security level, managed in CorsConfig
// .cors(corsConfig -> corsConfig.configurationSource(request -> null)) // Disable CORS at security level, managed in CorsConfig
@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = buildCorsConfiguration();

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(CORS_FILTER_ORDER); // Ensures the CORS filter runs before Spring Security

        return bean;
    }

    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(ALLOWED_ORIGINS);
        config.setAllowedHeaders(ALLOWED_HEADERS);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setExposedHeaders(EXPOSED_HEADERS);
        config.setMaxAge(MAX_AGE); // Cache pre-flight response for 1 hour

        return config;
    }
}
