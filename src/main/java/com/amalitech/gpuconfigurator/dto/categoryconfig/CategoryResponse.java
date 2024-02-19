package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

@Builder
public record CategoryResponse(String id, String name, String thumbnail) {
}
