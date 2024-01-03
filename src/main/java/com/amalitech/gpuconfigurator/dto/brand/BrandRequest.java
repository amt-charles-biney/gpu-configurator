package com.amalitech.gpuconfigurator.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BrandRequest(
        @NotBlank(message = "data must not be blank")
                @NotNull(message = "data must not be null")
        String name
) {
}
