package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductUpdateDto;

import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request);


    void deleteProductById(UUID id) throws NoSuchFieldException;

    CreateProductResponseDto getProductByProductId(String productId);

    GenericResponse updateProduct(UUID productId, ProductUpdateDto updatedProduct);
}
