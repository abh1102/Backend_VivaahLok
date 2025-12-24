package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.UserDTO;
import com.vivaahlok.vivahlok.dto.request.UpdateProfileRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "User Profile APIs")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserDTO> getProfile(@CurrentUser String userId) {
        UserDTO profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }
    
    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @CurrentUser String userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserDTO updatedUser = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updatedUser));
    }
    
    @PostMapping("/profile/image")
    @Operation(summary = "Upload profile image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @CurrentUser String userId,
            @RequestParam("file") MultipartFile file) {
        String imageUrl = userService.uploadProfileImage(userId, file);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
    
    @DeleteMapping("/profile/image")
    @Operation(summary = "Delete profile image")
    public ResponseEntity<ApiResponse<Void>> deleteProfileImage(@CurrentUser String userId) {
        userService.deleteProfileImage(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile image deleted successfully"));
    }
}
