package com.amalitech.gpuconfigurator.dto.email;

import lombok.Builder;

@Builder
public record EmailStockRequest(String email, String message, String productId) {
}
