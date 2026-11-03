package com.example.import_service.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String barCode;
    private String productName;
    private Long categoryId;
    private BigDecimal price;
    private int stock;
}
