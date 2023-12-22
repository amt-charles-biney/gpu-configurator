package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CompatibleOptionResponseDto(String id , String name, String type, BigDecimal price,
                                  String media, String unit, Boolean isCompatible, Boolean isIncluded) {
}
