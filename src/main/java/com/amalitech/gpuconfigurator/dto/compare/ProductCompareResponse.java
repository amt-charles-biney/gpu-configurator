package com.amalitech.gpuconfigurator.dto.compare;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record ProductCompareResponse(
        String productName,
        String productId,
        String description,
        String coverImage,
        String productCase,
        BigDecimal productPrice,

        Map<String, String> options

) {
}
