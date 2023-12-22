package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CompatibleOptionDTO(String name, String type, CategoryConfig categoryConfig, BigDecimal price,
                                  String media, String unit, Boolean isCompatible, Boolean isIncluded) {
}
