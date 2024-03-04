package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductResponse(
        String id,
        String productName,
        BigDecimal productPrice,
        List<String> imageUrl,

        String productCase,
        String coverImage,
        String productId,
        ProductResponseDto category,
        String productDescription,

        Double serviceCharge,

        boolean isFeatured,

        boolean productAvailability,

        Integer inStock,

<<<<<<< HEAD
        String stockStatus
=======
        String stockStatus,

        List<AttributeResponse> totalLeastStock
>>>>>>> 30cc83a (feat: variant indicators)
) {
}
