package com.amalitech.gpuconfigurator.util;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductResponseDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;

import java.util.List;

public class ResponseMapper {
    public List<ProductResponse> getProductResponses(List<Product> filteredProducts) {
        return filteredProducts.stream()
                .map(this::mapProductToProductResponse)
                .toList();
    }

    public ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
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
                .isWishListItem(product.isWishListItem())
                .build();
    }

    public ConfigurationResponseDto mapConfigurationToConfigurationResponseDto(Configuration configuredProduct) {
        Product product = configuredProduct.getProduct();

        return ConfigurationResponseDto.builder()
                .Id(String.valueOf(configuredProduct.getId()))
                .productId(String.valueOf(product.getId()))
                .productName(product.getProductName())
                .productPrice(product.getTotalProductPrice())
                .productDescription(product.getProductDescription())
                .productCoverImage(product.getProductCase().getCoverImageUrl())
                .productCaseName(product.getProductCase().getName())
                .totalPrice(configuredProduct.getTotalPrice())
                .warranty(null)
                .vat(null)
                .configuredPrice(null)
                .configured(configuredProduct.getConfiguredOptions())
                .quantity(configuredProduct.getQuantity())
                .configurationUrl(configuredProduct.getConfigurationUrl())
                .build();
    }
}
