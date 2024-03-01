package com.amalitech.gpuconfigurator.dto.categoryconfig;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import lombok.Builder;

@Builder
public record VariantStockLeastDto(String name, AttributeResponse attributeResponse, Integer inStock) {
}
