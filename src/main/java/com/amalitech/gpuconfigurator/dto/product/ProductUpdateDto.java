package com.amalitech.gpuconfigurator.dto.product;

public record ProductUpdateDto(
        String productName,
        String productDescription,
        Double productPrice,
        String productId,
        Boolean availability) {}