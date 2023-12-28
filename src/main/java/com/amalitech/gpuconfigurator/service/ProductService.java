package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request, List<MultipartFile> files);

    List<ProductResponse> getAllProducts();

    void deleteProductById(UUID id) throws NoSuchFieldException;

    Product getProductByProductId(String productId);
}
