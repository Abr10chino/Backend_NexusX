package com.example.import_service.exception;

import org.springframework.http.HttpStatus;

public final class ResouceNotFoundException extends ApiException{

    public ResouceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", message);
    }

    public ResouceNotFoundException(String resource, Object id) {
        this(resource + " not found with id " + id);
    }

}
