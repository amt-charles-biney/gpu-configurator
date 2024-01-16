package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record AttributeResponse(UUID id, String attributeName, Boolean isMeasured, String description, List<AttributeOptionResponseDto> attributeOptions) {
}
