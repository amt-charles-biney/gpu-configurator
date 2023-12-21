package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.category.Category;

import java.util.List;

public record CategoryConfigDto(List<CategoryOptionRequestDto> categoryConfigOptions, List<CompatibleOptionRequestDto> compatibleOptions) {
}
