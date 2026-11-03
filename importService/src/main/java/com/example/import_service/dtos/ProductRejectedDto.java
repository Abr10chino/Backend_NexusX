package com.example.import_service.dtos;

import lombok.Data;

@Data
public class ProductRejectedDto {

    private String barCode;
    private String reason;

}
