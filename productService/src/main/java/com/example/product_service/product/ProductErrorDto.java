package com.example.product_service.product;

public record ProductErrorDto (
        String barCode,
        String reason
){}
