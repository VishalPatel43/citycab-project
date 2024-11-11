package com.springboot.project.citycab.configs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Swagger UI can be accessed at http://localhost:8080/swagger-ui/index.html

    @Bean
    public OpenAPI cityCabOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CityCab Application API")
                        .description("API documentation for CityCab - a ride-hailing application built with Spring Boot 3 and Spring Security 6")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("CityCab GitHub Repository")
                        .url("https://github.com/VishalPatel43/citycab-project")); // Replace with actual repo link if available

    }
}
