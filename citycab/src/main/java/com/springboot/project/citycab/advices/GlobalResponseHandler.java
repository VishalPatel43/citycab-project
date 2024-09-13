package com.springboot.project.citycab.advices;

import org.springframework.core.MethodParameter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// It will be applied to all the controllers with the ResponseBody
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
//        if (body instanceof ApiResponse<?>)
        if (body instanceof ApiResponse<?> || body instanceof RepresentationModel<?>)
            return body;

        // Exclude /v3/api-docs endpoint from being wrapped
        if (request.getURI().getPath().contains("/v3/api-docs"))
            return body;
        // All the Response will be wrapped with ApiResponse
        return new ApiResponse<>(body);
    }
}
