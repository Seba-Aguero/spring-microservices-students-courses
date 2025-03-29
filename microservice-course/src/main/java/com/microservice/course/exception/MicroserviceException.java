package com.microservice.course.exception;

public class MicroserviceException extends RuntimeException {

    public MicroserviceException(String message) {
        super(message);
    }

    public MicroserviceException(String message, Throwable cause) {
        super(message, cause);
    }
}