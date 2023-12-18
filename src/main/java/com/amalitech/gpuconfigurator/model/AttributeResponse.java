package com.amalitech.gpuconfigurator.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttributeResponse(UUID id, String attributeName, String attributeType) {
}
