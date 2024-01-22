package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryConfigRequest(String name, List<CompatibleOptionDTO> config) {
}
