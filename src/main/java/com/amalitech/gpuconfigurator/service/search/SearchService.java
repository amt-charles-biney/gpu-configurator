package com.amalitech.gpuconfigurator.service.search;

import com.amalitech.gpuconfigurator.dto.search.ProductSearchResponseDto;

public interface SearchService {
    ProductSearchResponseDto findProducts(
            String query,
            Integer pageNo,
            Integer pageSize,
            String sortField,
            String[] brands,
            String[] priceRanges
    );
}
