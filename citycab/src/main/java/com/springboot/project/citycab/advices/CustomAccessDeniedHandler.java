package com.springboot.project.citycab.advices;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // Set 403 status and JSON content type
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Create a custom JSON error response
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .statusCode(HttpServletResponse.SC_FORBIDDEN)
                .message(accessDeniedException.getLocalizedMessage())
                .build();

        ApiResponse<ApiError> apiResponse = new ApiResponse<>(apiError);
        apiResponse.setPath(request.getRequestURI());

        // Write JSON error details to response
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
