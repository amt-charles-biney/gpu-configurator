package com.amalitech.gpuconfigurator.dto;

import org.springframework.lang.NonNull;

public record AttributeDto(
       @NonNull String name,
       @NonNull String type
) {
}
