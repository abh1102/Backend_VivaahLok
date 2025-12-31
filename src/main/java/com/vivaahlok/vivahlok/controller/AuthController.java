package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.dto.response.AuthResponse;
import com.vivaahlok.vivahlok.dto.response.TokenResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    
    @PostMapping("/signup")
    @Operation(summary = "Signup with Firebase ID token")
    public ResponseEntity<AuthResponse> signup(@RequestHeader("Authorization") String authorization) {
        String firebaseIdToken = authorization.replace("Bearer ", "");
        AuthResponse response = authService.signup(firebaseIdToken);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login with Firebase ID token")
    public ResponseEntity<AuthResponse> login(@RequestHeader("Authorization") String authorization) {
        String firebaseIdToken = authorization.replace("Bearer ", "");
        AuthResponse response = authService.login(firebaseIdToken);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<ApiResponse<Void>> logout(@CurrentUser String userId) {
        authService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
    
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        TokenResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
