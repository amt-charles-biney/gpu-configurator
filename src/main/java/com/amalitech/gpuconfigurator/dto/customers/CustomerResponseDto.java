package com.amalitech.gpuconfigurator.dto.customers;

import lombok.Builder;

@Builder
public record CustomerResponseDto(
        String name
) {
}
