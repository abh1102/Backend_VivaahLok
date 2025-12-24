package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.request.*;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.dto.response.AuthResponse;
import com.vivaahlok.vivahlok.dto.response.OtpResponse;
import com.vivaahlok.vivahlok.dto.response.TokenResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP to phone number")
    public ResponseEntity<OtpResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        OtpResponse response = authService.sendOtp(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP and login")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP")
    public ResponseEntity<OtpResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        OtpResponse response = authService.resendOtp(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register/user")
    @Operation(summary = "Register new user")
    public ResponseEntity<ApiResponse<Map<String, String>>> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        String userId = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", Map.of("userId", userId)));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<ApiResponse<Void>> logout(@CurrentUser String userId) {
        authService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
    
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
