package com.example.import_service.exception;

import org.springframework.http.HttpStatus;

public final class ConflictException extends ApiException{

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, "RESOURCE_CONFLICT", message);
    }

}
