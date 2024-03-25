package com.amalitech.gpuconfigurator.service.compare;

import com.amalitech.gpuconfigurator.dto.compare.ProductCompareResponse;

public interface CompareService {
    ProductCompareResponse getProductCompare(String productId);
}
