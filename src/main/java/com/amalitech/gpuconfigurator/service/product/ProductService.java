package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(String id);

    void deleteProductById(UUID id) throws NoSuchFieldException;

    Page<ProductResponse> getAllProducts(int page, int size, String sort);

    List<FeaturedProductDto> getNewProducts();


    ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto);
}