package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.request.RegisterDeviceRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.dto.response.NotificationsResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Push Notification APIs")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping("/register-device")
    @Operation(summary = "Register FCM token")
    public ResponseEntity<ApiResponse<Void>> registerDevice(
            @CurrentUser String userId,
            @Valid @RequestBody RegisterDeviceRequest request) {
        notificationService.registerDevice(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Device registered successfully"));
    }
    
    @GetMapping
    @Operation(summary = "Get notifications history")
    public ResponseEntity<NotificationsResponse> getNotifications(
            @CurrentUser String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        NotificationsResponse response = notificationService.getNotifications(userId, page, limit);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/read")
    @Operation(summary = "Mark as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable String id,
            @CurrentUser String userId) {
        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read"));
    }
    
    @PutMapping("/read-all")
    @Operation(summary = "Mark all as read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@CurrentUser String userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"));
    }
}
