package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryConfigResponseDto(String id, AttributeResponseDto category, List<CompatibleOptionResponseDto> options) {
}
