package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

@Builder
public record GenericResponse(Integer status, String msg) {
}
