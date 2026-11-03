package com.example.import_service.dtos;

import lombok.Data;

@Data
public class ApiResponseW <T>{

    private boolean success;
    private String message;
    private T data;

}
