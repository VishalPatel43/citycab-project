package com.springboot.project.citycab.exceptions;

public class DistanceRestClientServiceException extends RuntimeException {

    public DistanceRestClientServiceException() {
        super();
    }

    public DistanceRestClientServiceException(String message) {
        super(message);
    }

    public DistanceRestClientServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
