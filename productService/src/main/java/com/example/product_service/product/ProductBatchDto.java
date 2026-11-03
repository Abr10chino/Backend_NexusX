package com.example.product_service.product;

import java.util.List;

public record ProductBatchDto(
        List<ProductResponseDto> successfulProducts,
        List<ProductErrorDto> rejectedProducts
) { }
