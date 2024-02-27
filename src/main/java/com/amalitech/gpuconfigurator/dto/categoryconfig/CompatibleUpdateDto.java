package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

@Builder
public record CompatibleUpdateDto(String attributeId, String id, String attributeOptionId, boolean isMeasured, boolean isIncluded, boolean isCompatible, Integer size) {
}
