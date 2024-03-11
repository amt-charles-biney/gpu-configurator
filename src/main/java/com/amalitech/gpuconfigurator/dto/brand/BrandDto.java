package com.amalitech.gpuconfigurator.dto.brand;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BrandDto(String id, String description, String thumbnail, String name) {
}
