package com.amalitech.gpuconfigurator.dto.product;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.VariantStockLeastDto;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record ProductResponse(
        String id,
        String productName,
        BigDecimal productPrice,
        List<String> imageUrl,

        String productBrand,
        String coverImage,
        String productId,
        ProductResponseDto category,
        String productDescription,

        Double serviceCharge,

        boolean isFeatured,

        boolean productAvailability,

        Integer inStock,

        String stockStatus,

        List<AttributeResponse> totalLeastStock
) {
}
