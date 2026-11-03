package com.example.import_service.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImportResponseDto {

    private Long importId;
    private String fileName;
    private String status;
    private int totalRows;
    private int acceptedRows;
    private int rejectedRows;
    private LocalDateTime processedAt;
    private List<ProductRejectedDto> errors;

}
