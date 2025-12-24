package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.SettingsDTO;
import com.vivaahlok.vivahlok.dto.request.UpdateLocationRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateSettingsRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "Settings & Preferences APIs")
public class SettingsController {
    
    private final SettingsService settingsService;
    
    @GetMapping
    @Operation(summary = "Get user settings")
    public ResponseEntity<SettingsDTO> getSettings(@CurrentUser String userId) {
        SettingsDTO settings = settingsService.getSettings(userId);
        return ResponseEntity.ok(settings);
    }
    
    @PutMapping
    @Operation(summary = "Update settings")
    public ResponseEntity<ApiResponse<Void>> updateSettings(
            @CurrentUser String userId,
            @Valid @RequestBody UpdateSettingsRequest request) {
        settingsService.updateSettings(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Settings updated successfully"));
    }
    
    @PutMapping("/location")
    @Operation(summary = "Update location preference")
    public ResponseEntity<ApiResponse<Void>> updateLocation(
            @CurrentUser String userId,
            @Valid @RequestBody UpdateLocationRequest request) {
        settingsService.updateLocation(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Location updated successfully"));
    }
}
