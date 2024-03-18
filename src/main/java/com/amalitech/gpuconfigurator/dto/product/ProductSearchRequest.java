package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductSearchRequest(
        String query,
        List<String> cases,
        List<String> prices,
        List<String> brands,
        List<String> categories
) {
}
