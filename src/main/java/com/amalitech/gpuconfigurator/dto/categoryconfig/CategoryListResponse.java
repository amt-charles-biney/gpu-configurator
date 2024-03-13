package com.amalitech.gpuconfigurator.dto.categoryconfig;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Builder
public record CategoryListResponse(String name, String id, Map<String, List<CategoryConfigDisplay>> config, Long productCount) {
}