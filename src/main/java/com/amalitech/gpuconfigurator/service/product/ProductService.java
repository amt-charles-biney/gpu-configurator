package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.model.product.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request);


    void deleteProductById(UUID id) throws NoSuchFieldException;

    Product getProductByProductId(String productId);
}
