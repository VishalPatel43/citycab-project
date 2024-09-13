package com.springboot.project.citycab.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // This will exclude null fields from the response
public class ApiError {

    private HttpStatus status;
    private Integer statusCode;
    private String message;
    private List<String> subErrors;
    private String trace;
}
