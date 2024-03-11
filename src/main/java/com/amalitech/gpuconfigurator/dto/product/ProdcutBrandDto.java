package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProdcutBrandDto(
        String name,
        BigDecimal price
) {
}
