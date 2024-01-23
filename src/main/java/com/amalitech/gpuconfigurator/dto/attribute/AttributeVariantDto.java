package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

@Builder
public record AttributeVariantDto(Float baseAmount, Float maxAmount, Double priceFactor) {
}
