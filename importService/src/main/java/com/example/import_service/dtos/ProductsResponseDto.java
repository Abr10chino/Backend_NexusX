package com.example.import_service.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProductsResponseDto {

    private List<ProductSuccessDto> successfulProducts;
    private List<ProductRejectedDto> rejectedProducts;

}
