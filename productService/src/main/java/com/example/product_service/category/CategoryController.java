package com.example.product_service.category;

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
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    /* Endpoints de Busqueda */

    @Operation(summary = "Listar Todas las categorias", description = "Retorna una lista con todos los objetos de categorias que hay existentes")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No hay categorías registradas")
    })
    @GetMapping("categories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories(
            HttpServletRequest request
    ){
        log.info("Petición: Listando todas las categorias");

        List<CategoryResponseDto> categories = categoryService.findAllCategories();

        log.info("Éxito: Se encontraron {} categorías", categories.size());

        return ResponseEntity.ok(ResponseUtil.success(
                categories,
                "Categories List",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Buscar categoría por ID", description = "Retorna una categoría específica según su identificador único")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("category/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(
            @PathVariable Long id,
            HttpServletRequest request
    ){
        log.info("Petición: Buscando categoria por ID: {}", id);

        CategoryResponseDto category = categoryService.findById(id);

        log.info("Éxito: Categoria con ID {} encontrada", id);

        return ResponseEntity.ok(ResponseUtil.success(
                category,
                "Category Found",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Buscar categoría por nombre", description = "Retorna una categoría específica según su nombre")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("category")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryByName(
            @RequestParam String name,
            HttpServletRequest request
    ){
        log.info("Petición: Buscando categoria por nombre: {}", name);

        CategoryResponseDto category = categoryService.findByName(name);

        log.info("Éxito: Categoria con nombre '{}' encontrada", name);

        return ResponseEntity.ok(ResponseUtil.success(
                category,
                "Category Found",
                request.getRequestURI()
        ));
    }

    /* Endpoints de Creacion */

    @Operation(summary = "Crear una nueva categoría", description = "Registra una categoría individual validando que no esté duplicada")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Categoría creada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "La categoría ya está registrada")
    })
    @PostMapping("category")
    public ResponseEntity<ApiResponse<Category>> createCategory(
            @RequestBody CategoryRequestDto categoryRequestDto,
            HttpServletRequest request
    ){
        log.info("Petición: Creando categoria con nombre: {}", categoryRequestDto.name());

        Category created = categoryService.createCategory(categoryRequestDto);

        log.info("Éxito: Categoria '{}' creada correctamente", categoryRequestDto.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(
                created,
                "Category Created Successfully",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Creación de categorías", description = "Registra múltiples categorías a la vez validando duplicados")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Categorías creadas con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Alguna categoría ya está registrada")
    })
    @PostMapping("categories")
    public ResponseEntity<ApiResponse<List<Category>>> createCategories(
            @RequestBody List<CategoryRequestDto> categoryRequestDtos,
            HttpServletRequest request
    ){
        log.info("Petición: Creación de {} categorías", categoryRequestDtos.size());

        List<Category> createdList = categoryService.createCategories(categoryRequestDtos);

        log.info("Éxito: Se registraron {} categorías", createdList.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(
                createdList,
                "Categories Created Successfully",
                request.getRequestURI()
        ));
    }

    /* Endpoints de Actualizacion */

    @Operation(summary = "Actualizar categoría", description = "Modifica los datos de una categoría existente por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El nuevo nombre ya pertenece a otra categoría")
    })
    @PutMapping("category/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDto requestDto,
            HttpServletRequest request
    ){
        log.info("Petición: Actualizando categoria con ID: {}", id);

        CategoryResponseDto updated = categoryService.updateCategory(id, requestDto);

        log.info("Éxito: Categoria con ID {} actualizada correctamente", id);

        return ResponseEntity.ok(ResponseUtil.success(
                updated,
                "Category Updated Successfully",
                request.getRequestURI()
        ));
    }

    /* Endpoints de Eliminacion (Borrado Lógico) */

    @Operation(summary = "Eliminar categoría (Borrado lógico)", description = "Cambia el estatus de una categoría a INACTIVE por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría marcada como inactiva"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("category/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id,
            HttpServletRequest request
    ){
        log.info("Petición: Eliminando (lógicamente) categoria con ID: {}", id);

        categoryService.deleteCategory(id);

        log.info("Éxito: Categoria con ID {} pasada a INACTIVE", id);

        return ResponseEntity.ok(ResponseUtil.success(
                null,
                "Category Deleted Successfully",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Eliminación de categorías (Borrado lógico)", description = "Cambia el estatus a INACTIVE de múltiples categorías por sus IDs")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categorías marcadas como inactivas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se encontraron categorías para los IDs dados")
    })
    @DeleteMapping("categories")
    public ResponseEntity<ApiResponse<Void>> deleteCategories(
            @RequestBody List<Long> ids,
            HttpServletRequest request
    ){
        log.info("Petición: Eliminación (lógica) de {} categorías", ids.size());

        categoryService.deleteCategories(ids);

        log.info("Éxito: Se desactivaron las categorías con los IDs proporcionados");

        return ResponseEntity.ok(ResponseUtil.success(
                null,
                "Categories Deleted Successfully",
                request.getRequestURI()
        ));
    }

    @Operation(summary = "Reactivar categoría", description = "Cambia el estatus de una categoría a ACTIVE por su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoría reactivada con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })

    @PatchMapping("category/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> activateCategory(
            @PathVariable Long id,
            HttpServletRequest request
    ){
        log.info("Petición: Reactivando categoría con ID: {}", id);

        CategoryResponseDto activated = categoryService.activateCategory(id);

        log.info("Éxito: Categoría con ID {} pasada a ACTIVE", id);

        return ResponseEntity.ok(ResponseUtil.success(
                activated,
                "Category Activated Successfully",
                request.getRequestURI()
        ));
    }

}