package com.amalitech.gpuconfigurator.service.compare;

import com.amalitech.gpuconfigurator.dto.compare.ProductCompareResponse;

import java.util.List;

public interface CompareService {
    ProductCompareResponse getProductCompare(String productId);

    List<ProductCompareResponse> getProductCompareList(List<String> productIds);
}
