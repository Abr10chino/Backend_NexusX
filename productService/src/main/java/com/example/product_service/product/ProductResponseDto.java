package com.example.product_service.product;

import com.example.product_service.category.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto (

        Long id,
        String barCode,
        String productName,
        Category category,
        BigDecimal price,
        int stock,
        String status,
        LocalDateTime createdAt

){ }
