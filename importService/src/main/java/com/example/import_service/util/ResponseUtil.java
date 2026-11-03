package com.example.import_service.util;

import com.example.import_service.response.ApiResponse;

import java.time.Instant;
import java.util.List;

public final class ResponseUtil {

    private ResponseUtil(){}

    public static <T> ApiResponse<T> success(T data, String message, String path) {
        return new ApiResponse<>(true, message, data, List.of(), null, Instant.now(), path);
    }

    public static <T> ApiResponse<T> error(List<String> errors, String message, String errorCode, String path) {
        return new ApiResponse<>(false, message, null, errors, errorCode, Instant.now(), path);
    }

    public static <T> ApiResponse<T> error(String error, String message, String errorCode, String path) {
        return error(List.of(error), message, errorCode, path);
    }

}
