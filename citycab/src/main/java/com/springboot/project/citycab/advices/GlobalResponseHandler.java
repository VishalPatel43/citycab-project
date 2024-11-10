package com.springboot.project.citycab.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

// It will be applied to all the controllers with the ResponseBody
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    // Support every response
    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        List<String> allowedRoutes = List.of("/v3/api-docs", "/actuator");
        boolean isAllowed = allowedRoutes
                .stream()
                .anyMatch(route -> request.getURI().getPath().contains(route));

        log.info("Body: {}", body);

        if (body instanceof ApiResponse<?> || // --> ApiError fall here
                body instanceof String ||
                body instanceof RepresentationModel<?> ||
                isAllowed
        )
            return body;

        ApiResponse<Object> apiResponse = new ApiResponse<>(body);
        apiResponse.setPath(request.getURI().getPath()); // Set the path in the response

        log.info("Api Response created: {}", apiResponse);
        return apiResponse;
    }
}
