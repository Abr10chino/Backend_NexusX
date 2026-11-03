package com.example.product_service.product;

import java.math.BigDecimal;

public record ProductRequestDto(

        String barCode,
        String productName,
        Long categoryId,
        BigDecimal price,
        int stock

) { }
