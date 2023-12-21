package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CategoryConfigResponseDto(String id, AttributeResponseDto category, Map<String, List<CompatibleOptionResponseDto>> options) {
}
