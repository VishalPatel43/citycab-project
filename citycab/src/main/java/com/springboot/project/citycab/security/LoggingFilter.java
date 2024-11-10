package com.springboot.project.citycab.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String CLASS_NAME = "LoggingFilter";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logRequest(request);
        filterChain.doFilter(request, response);
        logResponse(response);
    }

    private void logRequest(HttpServletRequest request) {
        log.info("Incoming Request: Method={} URI={} from={} inside {}",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), CLASS_NAME);
    }

    private void logResponse(HttpServletResponse response) {
        log.info("Outgoing Response: status={} inside {}", response.getStatus(), CLASS_NAME);
    }
}
