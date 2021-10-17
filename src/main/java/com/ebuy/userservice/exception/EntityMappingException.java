package com.ebuy.userservice.exception;

public class EntityMappingException extends Exception {
    public EntityMappingException() {
        super();
    }

    public EntityMappingException(String message) {
        super(message);
    }

    public EntityMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
