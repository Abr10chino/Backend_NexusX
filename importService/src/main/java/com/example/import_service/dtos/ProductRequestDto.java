package com.example.import_service.dtos;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private Double price;
    private int stock;
    private String categoryName;
    private String barCode;
}
