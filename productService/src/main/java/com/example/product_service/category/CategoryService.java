package com.example.product_service.category;

import com.example.product_service.exception.ConflictException;
import com.example.product_service.exception.ResouceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){

        this.categoryRepository = categoryRepository;

    }

    /*Metodos para Busqueda*/

    public List<CategoryResponseDto> findAllCategories(){

        List<CategoryResponseDto> categories = categoryRepository.findAll().stream()
                .filter(category -> "ACTIVE".equalsIgnoreCase(category.getStatus()))
                .map(this::toEntity)
                .toList();

        if(categories.isEmpty()){

            throw new ResouceNotFoundException("No hay categorias en la base de datos");

        }

        return categories;

    }

    public CategoryResponseDto findById(Long id){

        return toEntity(categoryRepository.findById(id).orElseThrow(

                () -> new ResouceNotFoundException("Categoria con el ID: " + id + " no fue hallada.")

        ));

    }

    public CategoryResponseDto findByName(String name){

        return toEntity(categoryRepository.findByCategoryName(name).orElseThrow(
                () -> new ResouceNotFoundException("Categoria con el nombre: " + name + " no fue hallada.")
        ));

    }

    /*Metodos para Creacion*/

    public Category createCategory(CategoryRequestDto categoryRequestDto){

        if(!categoryRepository.existsByCategoryName(categoryRequestDto.name())){

            return categoryRepository.save(toEntity(categoryRequestDto));

        }

        throw new ConflictException("La Categoria: " + categoryRequestDto.name() + " ya esta registrada.");

    }

    public List<Category> createCategories(List<CategoryRequestDto> categoryRequestDtos){

        List<Category> categories = new ArrayList<>();
        CategoryRequestDto category;

        for(int i = 0; i < categoryRequestDtos.size(); i++){

            category = categoryRequestDtos.get(i);

            if(!categoryRepository.existsByCategoryName(category.name())){

                categories.add(toEntity(category));

            } else {

                throw new ConflictException("La Categoria: " + category.name() + " ya esta registrada.");

            }

        }

        return categoryRepository.saveAll(categories);

    }

    /*Metodos para Actualizar*/
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Categoria con el ID: " + id + " no fue hallada.")
        );

        if (!category.getCategoryName().equals(request.name()) && categoryRepository.existsByCategoryName(request.name())) {
            throw new ConflictException("La Categoria: " + request.name() + " ya esta registrada.");
        }

        category.setCategoryName(request.name());
        Category updated = categoryRepository.save(category);
        return toEntity(updated);
    }

    /*Metodos para Eliminar*/
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Categoria con el ID: " + id + " no fue hallada.")
        );

        category.setStatus("INACTIVE");
        categoryRepository.save(category);
    }

    public void deleteCategories(List<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);

        if (categories.isEmpty()) {
            throw new ResouceNotFoundException("No se encontraron categorias para los IDs proporcionados.");
        }

        for (Category category : categories) {
            category.setStatus("INACTIVE");
        }

        categoryRepository.saveAll(categories);
    }

    /*Metodos para castear Objetos a DTO*/

    private Category toEntity(CategoryRequestDto categoryRequestDto){

        Category category = new Category();

        category.setCategoryName(categoryRequestDto.name());
        category.setStatus("ACTIVE");

        return category;

    }

    private CategoryResponseDto toEntity(Category category){

        return new CategoryResponseDto(

                category.getId(),
                category.getCategoryName(),
                category.getStatus()

        );

    }

}
