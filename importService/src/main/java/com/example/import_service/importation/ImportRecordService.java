package com.example.import_service.importation;

import com.example.import_service.dtos.*;
import com.example.import_service.exception.ResouceNotFoundException;
import com.example.import_service.importationError.ImportError;
import com.example.import_service.importationError.ImportErrorRepository;
import com.example.import_service.response.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class ImportRecordService {

    private final ImportRecordRepository importRecordRepository;
    private final ImportErrorRepository importErrorRepository;
    private final RestTemplate restTemplate;

    public ImportRecordService(ImportRecordRepository importRecordRepository,
                               ImportErrorRepository importErrorRepository,
                               RestTemplate restTemplate){

        this.importRecordRepository = importRecordRepository;
        this.importErrorRepository = importErrorRepository;
        this.restTemplate = restTemplate;

    }

    /* Metodo de Mstrar Implementacion*/

    public List<ImportResponseDto> getAllImports(){
        List<ImportRecord> records = importRecordRepository.findAll();
        return records.stream().map(record -> {
            List<ImportError> dbErrors = importErrorRepository.findByImportRecordId(record.getId());
            List<ProductRejectedDto> errorDtos = dbErrors.stream().map(err -> {
                ProductRejectedDto dto = new ProductRejectedDto();
                dto.setBarCode(err.getRawData().replace("BarCode: ", ""));
                dto.setReason(err.getReason());
                return dto;
            }).toList();
            return toDto(record, errorDtos);
        }).toList();
    }

    public ImportResponseDto getImportById(Long id){
        ImportRecord record = importRecordRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Importación no encontrada con ID: " + id));

        List<ImportError> dbErrors = importErrorRepository.findByImportRecordId(id);
        List<ProductRejectedDto> errorDtos = dbErrors.stream().map(err -> {
            ProductRejectedDto dto = new ProductRejectedDto();
            dto.setBarCode(err.getRawData().replace("BarCode: ", ""));
            dto.setReason(err.getReason());
            return dto;
        }).toList();

        return toDto(record, errorDtos);
    }

    /* Metodo de Creacion de Implementacion*/

    @Transactional
    public ImportResponseDto processImport(String fileName, List<ProductRequestDto> products){

        ImportRecord record = toEntity(fileName, products.size());
        ImportRecord saveRecord = importRecordRepository.save(record);

        String apiCatologo = "http://product-service/v1/umes/products";

        ResponseEntity<ApiResponseW<ProductsResponseDto>> response = restTemplate.exchange(
                apiCatologo,
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(products),
                new org.springframework.core.ParameterizedTypeReference<ApiResponseW<ProductsResponseDto>>() {}
        );

        ApiResponseW<ProductsResponseDto> apiResponse = response.getBody();
        ProductsResponseDto responseDto = (apiResponse != null) ? apiResponse.getData() : null;

        int accepted = 0;
        int rejected = 0;
        List<ProductRejectedDto> rejectedErrors = List.of();

        if(responseDto != null){

            if(responseDto.getSuccessfulProducts() != null){
                accepted = responseDto.getSuccessfulProducts().size();
            }

            if(responseDto.getRejectedProducts() != null){
                rejectedErrors = responseDto.getRejectedProducts();
                rejected = rejectedErrors.size();

                List<ImportError> errors = toEntities(saveRecord, rejectedErrors);
                importErrorRepository.saveAll(errors);
            }

        }

        saveRecord.setAcceptedRows(accepted);
        saveRecord.setRejectedRows(rejected);
        saveRecord.setStatus("COMPLETED");
        ImportRecord finalRecord = importRecordRepository.save(saveRecord);

        return toDto(finalRecord, rejectedErrors);

    }

    private ImportRecord toEntity(String fileName, int totalRows){

        ImportRecord record = new ImportRecord();
        record.setFileName(fileName);
        record.setTotalRows(totalRows);
        return record;

    }

    private ImportError toEntity(ImportRecord record, ProductRejectedDto rejectedDto, int rowNumber) {
        ImportError error = new ImportError();
        error.setImportRecord(record);
        error.setRawData("BarCode: " + rejectedDto.getBarCode());
        error.setReason(rejectedDto.getReason());
        error.setRowNumber(rowNumber);
        return error;
    }

    private List<ImportError> toEntities(ImportRecord record, List<ProductRejectedDto> rejectedDtos) {
        return IntStream.range(0, rejectedDtos.size())
                .mapToObj(i -> toEntity(record, rejectedDtos.get(i), i + 1))
                .toList();
    }

    private ImportResponseDto toDto(ImportRecord record, List<ProductRejectedDto> errors) {
        ImportResponseDto responseDto = new ImportResponseDto();
        responseDto.setImportId(record.getId());
        responseDto.setFileName(record.getFileName());
        responseDto.setStatus(record.getStatus());
        responseDto.setTotalRows(record.getTotalRows());
        responseDto.setAcceptedRows(record.getAcceptedRows());
        responseDto.setRejectedRows(record.getRejectedRows());
        responseDto.setProcessedAt(record.getProcessedAt());
        responseDto.setErrors(errors);
        return responseDto;
    }

}
