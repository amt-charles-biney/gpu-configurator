package com.amalitech.gpuconfigurator.util;

import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductResponseDto;
import com.amalitech.gpuconfigurator.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ResponseMapper {
    public List<ProductResponse> getProductResponses(List<Product> filteredProducts) {
        return filteredProducts.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId().toString())
                        .productName(product.getProductName())
                        .productPrice(product.getTotalProductPrice())
                        .imageUrl(product.getProductCase().getImageUrls())
                        .productBrand(product.getProductCase().getName())
                        .coverImage(product.getProductCase().getCoverImageUrl())
                        .productId(product.getProductId())
                        .category(ProductResponseDto.builder().name(product.getCategory().getCategoryName())
                                .id(String.valueOf(product.getCategory().getId()))
                                .build())
                        .productDescription(product.getProductDescription())
                        .productAvailability(product.getProductAvailability())
                        .build())
                .toList();
    }
}
