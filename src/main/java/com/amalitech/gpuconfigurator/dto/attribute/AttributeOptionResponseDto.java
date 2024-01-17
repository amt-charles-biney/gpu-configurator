package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AttributeOptionResponseDto(String id, String optionName, AttributeVariantDto additionalInfo, BigDecimal optionPrice, String optionMedia, AttributeResponseDto attribute) {
}
