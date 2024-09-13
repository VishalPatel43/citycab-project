//package com.springboot.project.citycab.configs;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // registration and login APIs are accessible by all
//                .authorizeHttpRequests(
//                        auth -> auth
//                                // Access for all
//                                .requestMatchers("/api/user/login", "/api/user/register").permitAll()
//
//                                // add APIs that are only accessible by CUSTOMER, AGENT and ADMIN
//
//                                // add APIs that are only accessible by USER and ADMIN
//                                .requestMatchers("/api/users/profile")
//                                .hasAnyAuthority(USER_ROLE.ROLE_CUSTOMER.toString(),
//                                        USER_ROLE.ROLE_ADMIN.toString(),
//                                        USER_ROLE.ROLE_AGENT.toString())
//
////                                 add APIs that are only accessible by ADMIN
//                                .requestMatchers("/api/users/", "/api/users/**")
//                                .hasAuthority(USER_ROLE.ROLE_ADMIN.toString())
//
//                                .requestMatchers("/api/addresses/", "/api/addresses/**")
//                                .hasAuthority(USER_ROLE.ROLE_ADMIN.toString())
//
//                                .requestMatchers("/api/properties/", "/api/properties/**")
//                                .hasAuthority(USER_ROLE.ROLE_ADMIN.toString())
//
//                                .requestMatchers("/api/reviews/", "/api/reviews/**")
//                                .hasAuthority(USER_ROLE.ROLE_ADMIN.toString())
//
////                                .requestMatchers("/api/inquiries/", "/api/inquiries/**")
////                                .hasAuthority(USER_ROLE.ROLE_ADMIN.toString())
//
//                                .requestMatchers("api/admin/", "api/admin/**")
//                                .hasAuthority(USER_ROLE.ROLE_ADMIN.toString())
//
//                                .requestMatchers("api/agents/", "api/agents/**")
//                                .hasAuthority(USER_ROLE.ROLE_AGENT.toString())
//
//                                .requestMatchers("api/customers/", "api/customers/**")
//                                .hasAuthority(USER_ROLE.ROLE_CUSTOMER.toString())
//
//                                // remaining APIs are accessible by all or public (user)
//                                .anyRequest()
//                                .permitAll()
//                )
//                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//
//        return http.build();
//    }
//
//    private CorsConfigurationSource corsConfigurationSource() {
//        return new CorsConfigurationSource() {
//            @Override
//            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                CorsConfiguration cfg = new CorsConfiguration();
//                cfg.setAllowedOrigins(Arrays.asList( // allow all origins which frontend is running
//                        "http://localhost:3000",
//                        "http://localhost:4200"
//                ));
//                cfg.setAllowedMethods(Collections.singletonList("*")); // allow all methods => GET, POST, PUT, DELETE
//                cfg.setAllowCredentials(true);
//                cfg.setAllowedHeaders(Collections.singletonList("*"));
//                cfg.setExposedHeaders(Arrays.asList("Authorization"));
//                cfg.setMaxAge(3600L);
//                return cfg;
//            }
//        };
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
