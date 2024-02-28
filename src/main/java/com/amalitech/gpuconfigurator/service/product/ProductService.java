package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductUpdateDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponseWithBrandDto getProduct(String id);

    void deleteProductById(UUID id) throws NoSuchFieldException;

    Page<ProductResponse> getAllProducts(int page, int size,String sort);


    List<FeaturedProductDto> getNewProducts();


    @Transactional
    ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto);

    GenericResponse deleteBulkProducts(List<String> productIds);

    @Transactional
    void updateCategoryStock(UUID categoryId, UpdateProductConfigsDto request);

    @Transactional
    void updateTotalPriceWhenUpdatingCase(UUID caseId, BigDecimal casePrice);
}