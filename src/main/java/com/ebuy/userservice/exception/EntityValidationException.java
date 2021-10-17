package com.ebuy.userservice.exception;

public class EntityValidationException extends Exception {
    public EntityValidationException() {
        super();
    }

    public EntityValidationException(String message) {
        super(message);
    }
}
