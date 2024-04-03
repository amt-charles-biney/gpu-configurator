package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

@Builder
public record CancelledDto(String reason) {
}
