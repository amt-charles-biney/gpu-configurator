package com.amalitech.gpuconfigurator.service.search;

import com.amalitech.gpuconfigurator.dto.product.PageResponseDto;

public interface SearchService {
    PageResponseDto findProducts(
            String query,
            Integer pageNo,
            Integer pageSize,
            String sortField,
            String[] brands,
            String[] priceRanges
    );
}
