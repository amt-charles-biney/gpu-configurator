package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

@Builder
public record ProductResponseDto(String name, String id) {
}
