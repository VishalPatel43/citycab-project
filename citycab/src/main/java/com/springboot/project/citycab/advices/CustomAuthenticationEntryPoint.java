package com.springboot.project.citycab.advices;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .message("Unauthorized access. Please authenticate to proceed.")
//                .message(authException.getLocalizedMessage())
                .build();

        ApiResponse<ApiError> apiResponse = new ApiResponse<>(apiError);
        apiResponse.setPath(request.getRequestURI());

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}

