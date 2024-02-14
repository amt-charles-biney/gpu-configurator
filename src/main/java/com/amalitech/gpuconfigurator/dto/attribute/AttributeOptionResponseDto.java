package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record AttributeOptionResponseDto(String id, String optionName, AttributeVariantDto additionalInfo, BigDecimal optionPrice, String optionMedia, AttributeResponseDto attribute,
                                         List<IncompatibleAttributeResponse> incompatibleAttributes, String brand, Integer inStock) {
}
