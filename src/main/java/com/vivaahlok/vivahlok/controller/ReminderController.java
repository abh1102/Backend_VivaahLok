package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.ReminderDTO;
import com.vivaahlok.vivahlok.dto.request.CreateReminderRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateReminderRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
@Tag(name = "Reminders", description = "Reminder APIs")
public class ReminderController {
    
    private final ReminderService reminderService;
    
    @GetMapping
    @Operation(summary = "Get all reminders")
    public ResponseEntity<List<ReminderDTO>> getUserReminders(@CurrentUser String userId) {
        List<ReminderDTO> reminders = reminderService.getUserReminders(userId);
        return ResponseEntity.ok(reminders);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get reminder details")
    public ResponseEntity<ReminderDTO> getReminderById(
            @PathVariable String id,
            @CurrentUser String userId) {
        ReminderDTO reminder = reminderService.getReminderById(id, userId);
        return ResponseEntity.ok(reminder);
    }
    
    @PostMapping
    @Operation(summary = "Create reminder")
    public ResponseEntity<ApiResponse<Map<String, String>>> createReminder(
            @CurrentUser String userId,
            @Valid @RequestBody CreateReminderRequest request) {
        String reminderId = reminderService.createReminder(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Reminder created successfully", Map.of("reminderId", reminderId)));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update reminder")
    public ResponseEntity<ApiResponse<Void>> updateReminder(
            @PathVariable String id,
            @CurrentUser String userId,
            @Valid @RequestBody UpdateReminderRequest request) {
        reminderService.updateReminder(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Reminder updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reminder")
    public ResponseEntity<ApiResponse<Void>> deleteReminder(
            @PathVariable String id,
            @CurrentUser String userId) {
        reminderService.deleteReminder(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Reminder deleted successfully"));
    }
    
    @DeleteMapping
    @Operation(summary = "Delete all reminders")
    public ResponseEntity<ApiResponse<Void>> deleteAllReminders(@CurrentUser String userId) {
        reminderService.deleteAllReminders(userId);
        return ResponseEntity.ok(ApiResponse.success("All reminders deleted successfully"));
    }
}
