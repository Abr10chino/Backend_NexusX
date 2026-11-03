package com.example.product_service.product;

import com.example.product_service.category.Category;
import com.example.product_service.category.CategoryRepository;
import com.example.product_service.exception.ConflictException;
import com.example.product_service.exception.ResouceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /*Metodos para Busqueda*/

    public List<ProductResponseDto> findAllProducts() {
        List<ProductResponseDto> products = productRepository.findAll().stream()
                .filter(product -> "ACTIVE".equalsIgnoreCase(product.getStatus()))
                .map(this::toEntity)
                .toList();

        if (products.isEmpty()) {
            throw new ResouceNotFoundException("No hay productos en la base de datos");
        }

        return products;
    }

    public ProductResponseDto findById(Long id) {
        return toEntity(productRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Producto con el ID: " + id + " no fue hallado.")
        ));
    }

    public ProductResponseDto findByCode(String code) {
        return toEntity(productRepository.findByBarCode(code).orElseThrow(
                () -> new ResouceNotFoundException("Producto con el codigo: " + code + " no fue hallado.")
        ));
    }

    /*Metodos para Creacion*/

    public Product createProduct(ProductRequestDto productRequestDto) {
        if (productRepository.existsByBarCode(productRequestDto.barCode())) {
            throw new ConflictException("El producto con el codigo: " + productRequestDto.barCode() + " ya esta registrado.");
        }

        Category category = categoryRepository.findById(productRequestDto.categoryId()).orElseThrow(
                () -> new ResouceNotFoundException("Categoria con el ID: " + productRequestDto.categoryId() + " no fue hallada.")
        );

        return productRepository.save(toEntity(productRequestDto, category));
    }

    public ProductBatchDto createProducts(List<ProductRequestDto> requestDtos) {
        List<ProductResponseDto> successfulProducts = new java.util.ArrayList<>();
        List<ProductErrorDto> rejectedProducts = new java.util.ArrayList<>();

        for (ProductRequestDto dto : requestDtos) {
            if (productRepository.existsByBarCode(dto.barCode())) {
                rejectedProducts.add(new ProductErrorDto(dto.barCode(), "El producto con el codigo ya esta registrado."));
                continue;
            }

            var categoryOpt = categoryRepository.findById(dto.categoryId());
            if (categoryOpt.isEmpty()) {
                rejectedProducts.add(new ProductErrorDto(dto.barCode(), "Categoria con el ID: " + dto.categoryId() + " no fue hallada."));
                continue;
            }

            Product product = toEntity(dto, categoryOpt.get());
            Product saved = productRepository.save(product);
            successfulProducts.add(toEntity(saved));
        }

        return new ProductBatchDto(successfulProducts, rejectedProducts);
    }

    /*Metodos para Actualizar*/

    public ProductResponseDto updateProduct(Long id, ProductRequestDto request) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Producto con el ID: " + id + " no fue hallado.")
        );

        if (!product.getBarCode().equals(request.barCode()) && productRepository.existsByBarCode(request.barCode())) {
            throw new ConflictException("El producto con el codigo: " + request.barCode() + " ya esta registrado.");
        }

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(
                () -> new ResouceNotFoundException("Categoria con el ID: " + request.categoryId() + " no fue hallada.")
        );

        product.setBarCode(request.barCode());
        product.setProductName(request.productName());
        product.setCategory(category);
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product updated = productRepository.save(product);
        return toEntity(updated);
    }

    public ProductResponseDto activateProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Producto con el ID: " + id + " no fue hallado.")
        );

        product.setStatus("ACTIVE");
        Product savedProduct = productRepository.save(product);

        return toEntity(savedProduct);
    }

    /*Metodos para Eliminar*/

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Producto con el ID: " + id + " no fue hallado.")
        );

        product.setStatus("INACTIVE");
        productRepository.save(product);
    }

    public void deleteProducts(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);

        if (products.isEmpty()) {
            throw new ResouceNotFoundException("No se encontraron productos para los IDs proporcionados.");
        }

        for (Product product : products) {
            product.setStatus("INACTIVE");
        }

        productRepository.saveAll(products);
    }

    /*Metodos para castear Objetos a DTO*/

    private Product toEntity(ProductRequestDto productRequestDto, Category category) {
        Product product = new Product();
        product.setBarCode(productRequestDto.barCode());
        product.setProductName(productRequestDto.productName());
        product.setCategory(category);
        product.setPrice(productRequestDto.price());
        product.setStock(productRequestDto.stock());
        product.setStatus("ACTIVE");
        return product;
    }

    private ProductResponseDto toEntity(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getBarCode(),
                product.getProductName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreatedAt()
        );
    }

}
