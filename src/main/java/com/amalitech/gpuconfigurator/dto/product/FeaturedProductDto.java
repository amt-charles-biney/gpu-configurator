package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

@Builder
public record FeaturedProductDto(
        String productName,
        String coverImage
) {
}
