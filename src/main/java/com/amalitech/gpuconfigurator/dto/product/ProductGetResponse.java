package com.amalitech.gpuconfigurator.dto.product;

import com.amalitech.gpuconfigurator.model.Case;
import com.amalitech.gpuconfigurator.model.Category;

import java.math.BigDecimal;

public record ProductGetResponse(
        String id,
        String productName,
        String productId,
        String productDescription,
        Double serviceCharge,
        BigDecimal totalProductPrice,
        BigDecimal baseConfigPrice,
        Case productCase,
        Category category,
        Boolean featured,
        Boolean productAvailability
) {}