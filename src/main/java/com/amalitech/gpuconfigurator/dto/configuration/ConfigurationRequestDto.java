package com.amalitech.gpuconfigurator.dto.configuration;

import lombok.Builder;

@Builder
public record ConfigurationRequestDto(String productId, String categoryId) {}
