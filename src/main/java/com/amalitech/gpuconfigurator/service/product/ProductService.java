package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request);

    List<ProductResponse> getAllProducts();

    List<ProductResponseDto> getAllProductsCompare();

    ProductResponseWithBrandDto getProduct(String id);

    void deleteProductById(UUID id) throws NoSuchFieldException;

    Page<ProductResponse> getAllProducts(int page, int size, String sort);

    List<FeaturedProductDto> getNewProducts();


    Page<ProductResponse> getAllProductsAdmin(Integer page, Integer size, String query);

    @Transactional
    ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto);

    GenericResponse deleteBulkProducts(List<String> productIds);

    @Transactional
    void updateCategoryStock(UUID categoryId, UpdateProductConfigsDto request);

    @Transactional
    void updateTotalPriceWhenUpdatingCase(UUID caseId, BigDecimal casePrice);

    Page<ProductResponse> getAllProductsUsers(int page, int size, ProductSearchRequest dto, User user, UserSession userSession);
}