package com.example.import_service.importation;

import com.example.import_service.dtos.ImportResponseDto;
import com.example.import_service.dtos.ProductRequestDto;
import com.example.import_service.response.ApiResponse;
import com.example.import_service.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/umes/imports")
public class ImportRecordController {

    private static final Logger log = LoggerFactory.getLogger(ImportRecordController.class);

    private final ImportRecordService importRecordService;

    public ImportRecordController(ImportRecordService importRecordService){

        this.importRecordService = importRecordService;

    }

    @Operation(summary = "Procesar importacion de productos", description = "Recibe un lote de productos y los manda al catalogo para su registro masivo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Importacion procesada con exito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cuerpo de la peticion mal formado")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ImportResponseDto>> createImport(

            @RequestParam String fileName,
            @RequestBody List<ProductRequestDto> requestDtos,
            HttpServletRequest request

    ){

        log.info("Peticion: Procesando archivo de importacion '{}' con {} registros", fileName, requestDtos.size());

        ImportResponseDto responseDto = importRecordService.processImport(fileName, requestDtos);

        log.info("Exito: Importacion procesada. Aceptados: {}, Rechazados: {}",
                responseDto.getAcceptedRows(), responseDto.getRejectedRows());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(

                responseDto,
                "Import Processed Successfully",
                request.getRequestURI()

        ));

    }

    @Operation(summary = "Obtener historial de importaciones", description = "Devuelve la lista de todas las importaciones procesadas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ImportResponseDto>>> getAllImports(HttpServletRequest request) {
        log.info("Peticion: Consultando historial de todas las importaciones");
        List<ImportResponseDto> responseDtos = importRecordService.getAllImports();
        return ResponseEntity.ok(ResponseUtil.success(
                responseDtos,
                "Imports Retrieved Successfully",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Obtener detalles de una importación", description = "Devuelve el resumen y errores de una importación específica por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ImportResponseDto>> getImportById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Peticion: Consultando importacion con ID: {}", id);
        ImportResponseDto responseDto = importRecordService.getImportById(id);
        return ResponseEntity.ok(ResponseUtil.success(
                responseDto,
                "Import Details Retrieved Successfully",
                request.getRequestURI()
        ));
    }

}
