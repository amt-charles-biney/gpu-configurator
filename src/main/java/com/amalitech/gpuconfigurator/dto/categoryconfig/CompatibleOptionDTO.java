package com.amalitech.gpuconfigurator.dto.categoryconfig;

import com.amalitech.gpuconfigurator.model.CategoryConfig;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CompatibleOptionDTO(CategoryConfig categoryConfig,
                                  @NotNull(message = "attribute option id must exixt")
                                  String attributeOptionId, Boolean isCompatible, Boolean isIncluded, boolean isMeasured, Integer size, String attributeId) {
}
