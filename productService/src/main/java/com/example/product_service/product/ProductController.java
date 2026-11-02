package com.example.product_service.product;

import com.example.product_service.response.ApiResponse;
import com.example.product_service.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/umes/")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /* Endpoints de Busqueda */

    @Operation(summary = "Listar todos los productos", description = "Devuelve la lista de todos los productos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay productos")
    })
    @GetMapping("products")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts(
            HttpServletRequest request
    ) {
        log.info("Petición: Listando todos los productos");

        List<ProductResponseDto> products = productService.findAllProducts();

        log.info("Éxito: Se encontraron {} productos", products.size());

        return ResponseEntity.ok(ResponseUtil.success(
                products,
                "Products List",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Buscar producto por ID", description = "Devuelve un producto buscando por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("products/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Petición: Buscando producto por ID: {}", id);

        ProductResponseDto product = productService.findById(id);

        log.info("Éxito: Producto con ID {} encontrado", id);

        return ResponseEntity.ok(ResponseUtil.success(
                product,
                "Product Found",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Buscar producto por codigo", description = "Devuelve un producto buscando por su codigo de barras")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("products/search")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductByCode(
            @RequestParam String code,
            HttpServletRequest request
    ) {
        log.info("Petición: Buscando producto por codigo: {}", code);

        ProductResponseDto product = productService.findByCode(code);

        log.info("Éxito: Producto con codigo '{}' encontrado", code);

        return ResponseEntity.ok(ResponseUtil.success(
                product,
                "Product Found",
                request.getRequestURI()
        ));
    }

    /* Endpoints de Creacion */

    @Operation(summary = "Crear un producto", description = "Guarda un producto nuevo validando que el codigo no exista")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Producto creado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El codigo ya existe")
    })
    @PostMapping("products")
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @RequestBody ProductRequestDto requestDto,
            HttpServletRequest request
    ) {
        log.info("Petición: Creando producto con codigo: {}", requestDto.barCode());

        Product created = productService.createProduct(requestDto);

        log.info("Éxito: Producto con codigo '{}' creado correctamente", requestDto.barCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(
                created,
                "Product Created Successfully",
                request.getRequestURI()
        ));
    }

    /* Endpoints de Actualizacion */

    @Operation(summary = "Actualizar producto", description = "Modifica un producto existente por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto actualizado con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El codigo ya existe en otro producto")
    })
    @PutMapping("products/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDto requestDto,
            HttpServletRequest request
    ) {
        log.info("Petición: Actualizando producto con ID: {}", id);

        ProductResponseDto updated = productService.updateProduct(id, requestDto);

        log.info("Éxito: Producto con ID {} actualizado", id);

        return ResponseEntity.ok(ResponseUtil.success(
                updated,
                "Product Updated Successfully",
                request.getRequestURI()
        ));
    }

    /* Endpoints de Eliminar */

    @Operation(summary = "Eliminar producto", description = "Cambia el estado del producto a INACTIVE por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto eliminado (inactivo)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Petición: Eliminando producto con ID: {}", id);

        productService.deleteProduct(id);

        log.info("Éxito: Producto con ID {} pasado a INACTIVE", id);

        return ResponseEntity.ok(ResponseUtil.success(
                null,
                "Product Deleted Successfully",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Eliminar varios productos", description = "Cambia el estado a INACTIVE de varios productos a la vez")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Productos eliminados (inactivos)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se encontraron los productos")
    })
    @DeleteMapping("products/batch")
    public ResponseEntity<ApiResponse<Void>> deleteProducts(
            @RequestBody List<Long> ids,
            HttpServletRequest request
    ) {
        log.info("Petición: Eliminando {} productos de un solo", ids.size());

        productService.deleteProducts(ids);

        log.info("Éxito: Productos desactivados correctamente");

        return ResponseEntity.ok(ResponseUtil.success(
                null,
                "Products Deleted Successfully",
                request.getRequestURI()
        ));
    }
}
