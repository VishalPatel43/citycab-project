package com.springboot.project.citycab.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // This will exclude null fields from the response
public class ApiResponse<T> {

    // write time in 24-hours format
    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime timeStamp;

    // either data or error will be null
    private T data;
    private ApiError error;
    private String path;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }
}
