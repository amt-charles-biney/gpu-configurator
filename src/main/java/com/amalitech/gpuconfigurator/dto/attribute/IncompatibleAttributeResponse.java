package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record IncompatibleAttributeResponse(String id, String optionName, AttributeVariantDto additionalInfo, BigDecimal optionPrice, String optionMedia, AttributeResponseDto attribute, String brand) {
}
