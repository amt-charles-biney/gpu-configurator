package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductResponseWithBrandDto(
        String id,
        String productName,
        BigDecimal productPrice,
        List<String> imageUrl,

        ProdcutBrandDto productBrand,
        String coverImage,
        String productId,
        ProductResponseDto category,
        String productDescription,

        Double serviceCharge,

        boolean isFeatured,

        boolean productAvailability,

        Integer inStock,

        String stockStatus
) {
}
