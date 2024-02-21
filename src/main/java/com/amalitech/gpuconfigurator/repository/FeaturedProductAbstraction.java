package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Case;

public interface FeaturedProductAbstraction {

    String getId();
    String getProductName();

    Case getProductCase();
}
