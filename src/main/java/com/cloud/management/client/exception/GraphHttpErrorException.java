package com.cloud.management.client.exception;

public class GraphHttpErrorException extends RuntimeException {
    public GraphHttpErrorException(String message) {
        super(message);
    }

    public GraphHttpErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
