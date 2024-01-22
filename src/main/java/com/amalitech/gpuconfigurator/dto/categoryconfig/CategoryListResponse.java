package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryListResponse(String name, String id, List<String> config, Long productCount) {
}
