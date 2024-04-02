package com.amalitech.gpuconfigurator.controller.notification;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.notification.NotificationResponse;
import com.amalitech.gpuconfigurator.service.notification.NotificationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Notification")
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;
    @Operation(
            description = "Notification for admin",
            summary = "This is for getting all notification",
            method = "GET"
    )
    @GetMapping("/v1/admin/notifications")
    public ResponseEntity<ApiResponse<NotificationResponse>> getAllAdminNotifications() {
        NotificationResponse notificationResponse = notificationService.getAllAdminFixNotifications();
        return ResponseEntity.ok(new ApiResponse<>(notificationResponse));
    }
}
