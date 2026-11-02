package com.example.product_service.exception;

import org.springframework.http.HttpStatus;

public final class ConflictException extends ApiException{

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, "RESOURCE_CONFLICT", message);
    }

    public static ConflictException duplicateEmail(String email) {
        return new ConflictException("Email already exists: " + email);
    }

}
