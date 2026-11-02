package com.example.product_service.exception;

import org.springframework.http.HttpStatus;

public sealed class ApiException extends RuntimeException
        permits ResouceNotFoundException, ConflictException{

    private final HttpStatus status;
    private final String errorCode;

    public ApiException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
