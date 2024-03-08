package com.amalitech.gpuconfigurator.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCartItemQuantityRequest {
    @NotNull
    private UUID configuredProductId;

    @Positive
    private int quantity;
}
