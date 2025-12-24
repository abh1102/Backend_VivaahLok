package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDeviceRequest {
    @NotBlank(message = "FCM token is required")
    private String fcmToken;
    
    @NotBlank(message = "Platform is required")
    private String platform;
}
