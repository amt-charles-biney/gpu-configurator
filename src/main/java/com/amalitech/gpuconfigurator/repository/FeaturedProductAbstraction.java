package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Case;

import java.math.BigDecimal;

public interface FeaturedProductAbstraction {

    String getId();

    String getProductName();

    Case getProductCase();

    BigDecimal getTotalProductPrice();

    Case getCase();
}
