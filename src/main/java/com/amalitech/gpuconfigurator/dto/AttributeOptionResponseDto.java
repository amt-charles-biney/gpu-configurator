package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.Attribute;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AttributeOptionResponseDto(String id, String optionName, BigDecimal optionPrice, AttributeResponseDto attribute) {
}
