package com.springboot.project.citycab.exceptions;

public class OSRMServiceException extends RuntimeException {

    public OSRMServiceException() {
        super();
    }

    public OSRMServiceException(String message) {
        super(message);
    }

    public OSRMServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
