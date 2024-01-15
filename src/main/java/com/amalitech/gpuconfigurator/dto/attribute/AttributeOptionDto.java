package com.amalitech.gpuconfigurator.dto.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AttributeOptionDto(
        @NotNull(message = "attribute option cannot be null")
                @NotBlank(message = "attribute option cannot blank")
        String name, BigDecimal price, String media, String unit, Float baseAmount, Float maxAmount, Float priceIncrement) {
}
