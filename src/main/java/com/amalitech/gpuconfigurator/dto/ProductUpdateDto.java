package com.amalitech.gpuconfigurator.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateDto(
        String productName,
        String productDescription,
        Double productPrice,
        String productId,
        Boolean availability) {}