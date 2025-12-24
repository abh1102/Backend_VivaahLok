package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.WishlistDTO;
import com.vivaahlok.vivahlok.dto.request.AddWishlistRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist/Favorites APIs")
public class WishlistController {
    
    private final WishlistService wishlistService;
    
    @GetMapping
    @Operation(summary = "Get user's wishlist")
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(@CurrentUser String userId) {
        List<WishlistDTO> wishlist = wishlistService.getUserWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }
    
    @PostMapping
    @Operation(summary = "Add to wishlist")
    public ResponseEntity<ApiResponse<Map<String, String>>> addToWishlist(
            @CurrentUser String userId,
            @Valid @RequestBody AddWishlistRequest request) {
        String wishlistId = wishlistService.addToWishlist(userId, request.getVendorId());
        return ResponseEntity.ok(ApiResponse.success("Added to wishlist", Map.of("wishlistId", wishlistId)));
    }
    
    @DeleteMapping("/{vendorId}")
    @Operation(summary = "Remove from wishlist")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @CurrentUser String userId,
            @PathVariable String vendorId) {
        wishlistService.removeFromWishlist(userId, vendorId);
        return ResponseEntity.ok(ApiResponse.success("Removed from wishlist"));
    }
}
