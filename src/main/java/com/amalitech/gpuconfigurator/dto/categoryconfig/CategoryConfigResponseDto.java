package com.amalitech.gpuconfigurator.dto.categoryconfig;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CategoryConfigResponseDto(
        String id,
        CategoryResponse category,
        Map<String, List<CompatibleOptionResponseDto>> options,
        Integer inStock,
        List<VariantStockLeastDto> totalLeastStocks,
        List<CaseResponse> cases) {
}
