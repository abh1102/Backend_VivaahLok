package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.CardDesignDTO;
import com.vivaahlok.vivahlok.dto.InvitationCardDTO;
import com.vivaahlok.vivahlok.dto.request.CreateCardRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.dto.response.ShareResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Invitation Cards", description = "Invitation Card APIs")
public class CardController {
    
    private final CardService cardService;
    
    @GetMapping("/designs")
    @Operation(summary = "Get card design templates")
    public ResponseEntity<List<CardDesignDTO>> getCardDesigns() {
        List<CardDesignDTO> designs = cardService.getCardDesigns();
        return ResponseEntity.ok(designs);
    }
    
    @PostMapping
    @Operation(summary = "Create invitation card")
    public ResponseEntity<ApiResponse<Map<String, String>>> createCard(
            @CurrentUser String userId,
            @Valid @RequestBody CreateCardRequest request) {
        String cardId = cardService.createCard(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Card created successfully", 
                Map.of("cardId", cardId, "cardUrl", "/cards/view/" + cardId)));
    }
    
    @GetMapping
    @Operation(summary = "Get user's created cards")
    public ResponseEntity<List<InvitationCardDTO>> getUserCards(@CurrentUser String userId) {
        List<InvitationCardDTO> cards = cardService.getUserCards(userId);
        return ResponseEntity.ok(cards);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get card details")
    public ResponseEntity<InvitationCardDTO> getCardById(
            @PathVariable String id,
            @CurrentUser String userId) {
        InvitationCardDTO card = cardService.getCardById(id, userId);
        return ResponseEntity.ok(card);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete card")
    public ResponseEntity<ApiResponse<Void>> deleteCard(
            @PathVariable String id,
            @CurrentUser String userId) {
        cardService.deleteCard(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Card deleted successfully"));
    }
    
    @GetMapping("/{id}/share")
    @Operation(summary = "Get shareable link")
    public ResponseEntity<ShareResponse> getShareLink(
            @PathVariable String id,
            @CurrentUser String userId) {
        ShareResponse response = cardService.getShareLink(id, userId);
        return ResponseEntity.ok(response);
    }
}
