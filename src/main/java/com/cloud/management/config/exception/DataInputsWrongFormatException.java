package com.cloud.management.config.exception;

public class DataInputsWrongFormatException extends RuntimeException {
    public DataInputsWrongFormatException(String message) {
        super(message);
    }

    public DataInputsWrongFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
