package com.amalitech.gpuconfigurator.dto.product;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import com.amalitech.gpuconfigurator.model.Category;
import lombok.Builder;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductResponse(
        String id,
        String productName,
        BigDecimal productPrice,
        List<String> imageUrl,

        String coverImage,
        String productId,
        AttributeResponseDto category,
        String productDescription,

        boolean productAvailability
) {
}
