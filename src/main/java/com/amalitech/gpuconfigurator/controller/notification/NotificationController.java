package com.amalitech.gpuconfigurator.controller.notification;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.notification.NotificationResponse;
import com.amalitech.gpuconfigurator.service.notification.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;
    @GetMapping("/v1/admin/notifications")
    public ResponseEntity<ApiResponse<NotificationResponse>> getAllAdminNotifications() {
        NotificationResponse notificationResponse = notificationService.getAllAdminFixNotifications();
        return ResponseEntity.ok(new ApiResponse<>(notificationResponse));
    }
}
