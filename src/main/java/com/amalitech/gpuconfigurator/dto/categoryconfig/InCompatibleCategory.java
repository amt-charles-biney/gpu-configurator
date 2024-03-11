package com.amalitech.gpuconfigurator.dto.categoryconfig;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record InCompatibleCategory(String id, String optionName, AdditionalInfoDto additionalInfo, BigDecimal optionPrice, String optionMedia, AttributeResponseDto attribute) {
}
