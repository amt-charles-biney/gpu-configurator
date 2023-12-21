package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AttributeOptionResponseDto(String id, String optionName, BigDecimal optionPrice, AttributeResponseDto attribute) {
}
