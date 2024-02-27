package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request);

    List<ProductResponse> getAllProducts();

    ProductResponseWithBrandDto getProduct(String id);

    void deleteProductById(UUID id) throws NoSuchFieldException;

    Page<ProductResponse> getAllProducts(int page, int size, String sort);

    List<FeaturedProductDto> getNewProducts();


    ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto);

    GenericResponse deleteBulkProducts(List<String> productIds);

    void updateCategoryStock(UUID categoryId, Integer stock);
}