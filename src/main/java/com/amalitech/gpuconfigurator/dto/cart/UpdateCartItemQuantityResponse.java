package com.amalitech.gpuconfigurator.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemQuantityResponse {
    private String message;

    private UUID configuredProductId;

    private int quantity;
}
