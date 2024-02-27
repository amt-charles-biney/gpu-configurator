package com.amalitech.gpuconfigurator.dto.categoryconfig;


import lombok.Builder;

import java.util.List;

@Builder
public record CompatibleOptionEditResponse(String name, String id, String thumbnail, List<CompatibleUpdateDto> config) {
}
