package com.amalitech.gpuconfigurator.dto.compare;

import lombok.Builder;

import java.util.Map;

@Builder
public record ProductCompareResponse(
        String productName,
        String productId,
        String description,
        String coverImage,

        Map<String, String> options

) {
}
