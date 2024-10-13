package com.springboot.project.citycab.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    private static final Long MAX_AGE = 3600L; // 1 hour
    private static final int CORS_FILTER_ORDER = -102; // CORS filter must run before Spring Security

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
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "https://citycab.herokuapp.com"
        ));
//        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
//        config.setAllowedMethods(Collections.singletonList("*")); // allow all methods => GET, POST, PUT, DELETE
        config.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
        ));
//        config.setExposedHeaders(List.of("Authorization"));
        config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
        config.setMaxAge(MAX_AGE); // Cache pre-flight response for 1 hour
        return config;
    }
}
