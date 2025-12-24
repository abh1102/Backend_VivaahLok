package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phone;
    
    @NotBlank(message = "OTP is required")
    private String otp;
    
    @NotBlank(message = "OTP ID is required")
    private String otpId;
}
