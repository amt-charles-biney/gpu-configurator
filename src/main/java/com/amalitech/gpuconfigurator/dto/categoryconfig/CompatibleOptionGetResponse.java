package com.amalitech.gpuconfigurator.dto.categoryconfig;


import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CompatibleOptionGetResponse(String name, String id, String thumbnail, List<CompatibleOptionResponseDto> config, Integer inStock, List<VariantStockLeastDto> totalLeastStocks, double configPrice, List<CaseResponse> cases) {
}