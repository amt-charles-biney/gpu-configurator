package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryConfigRequest(String name, String thumbnail,  List<CompatibleOptionDTO> config, List<String> caseIds) {
}
