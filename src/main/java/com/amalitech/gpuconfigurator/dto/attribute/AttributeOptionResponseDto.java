package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AttributeOptionResponseDto(String id, String optionName, BigDecimal optionPrice, String optionMedia, Integer optionUnit, AttributeResponseDto attribute) {
}
