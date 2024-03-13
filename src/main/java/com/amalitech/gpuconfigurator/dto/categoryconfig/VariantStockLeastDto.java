package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

@Builder
public record VariantStockLeastDto(String name, String attributeResponse, Integer inStock) {
}
