package com.example.import_service.response;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(

        boolean success,
        String message,
        T data,
        List<String> errors,
        String errorCode,
        Instant timestamp,
        String path

) {

    public ApiResponse{

        errors = errors == null ? List.of() : List.copyOf(errors);

    }

}
