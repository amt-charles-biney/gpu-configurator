package com.amalitech.gpuconfigurator.dto;

public record ApiResponse<T>(T data, String message, String status) {
}
