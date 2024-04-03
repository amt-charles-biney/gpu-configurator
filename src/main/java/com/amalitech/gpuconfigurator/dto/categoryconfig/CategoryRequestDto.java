package com.amalitech.gpuconfigurator.dto.categoryconfig;

import java.util.List;

public record CategoryRequestDto(String name, String thumbnail, List<String> caseIds) {
}
