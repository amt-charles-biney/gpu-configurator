package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

@Builder
public record ApiResponse<T>(T data, String message, String status) {

    public ApiResponse(T data) {
        this(data, "", "success");
    }
}
