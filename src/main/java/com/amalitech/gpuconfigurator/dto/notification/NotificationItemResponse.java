package com.amalitech.gpuconfigurator.dto.notification;

public record NotificationItemResponse<T>(String name, String id, String message, T content) {
    public NotificationItemResponse(String name, String id, String message) {
        this(name, id, message, null);
    }
}
