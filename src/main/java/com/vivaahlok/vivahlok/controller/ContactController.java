package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.ContactDTO;
import com.vivaahlok.vivahlok.dto.request.ContactVendorRequest;
import com.vivaahlok.vivahlok.dto.request.SyncContactsRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Contact/Communication APIs")
public class ContactController {
    
    private final ContactService contactService;
    
    @PostMapping("/contact/vendor")
    @Operation(summary = "Contact vendor (log inquiry)")
    public ResponseEntity<ApiResponse<Void>> contactVendor(
            @CurrentUser String userId,
            @Valid @RequestBody ContactVendorRequest request) {
        contactService.contactVendor(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Inquiry logged successfully"));
    }
    
    @GetMapping("/contacts")
    @Operation(summary = "Get user's contacts (for reminders)")
    public ResponseEntity<List<ContactDTO>> getUserContacts(@CurrentUser String userId) {
        List<ContactDTO> contacts = contactService.getUserContacts(userId);
        return ResponseEntity.ok(contacts);
    }
    
    @PostMapping("/contacts/sync")
    @Operation(summary = "Sync contacts from device")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> syncContacts(
            @CurrentUser String userId,
            @Valid @RequestBody SyncContactsRequest request) {
        int syncedCount = contactService.syncContacts(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Contacts synced successfully", Map.of("syncedCount", syncedCount)));
    }
}
