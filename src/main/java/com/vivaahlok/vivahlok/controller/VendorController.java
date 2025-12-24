package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.VendorDTO;
import com.vivaahlok.vivahlok.dto.VendorListDTO;
import com.vivaahlok.vivahlok.dto.request.AddReviewRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.dto.response.PageResponse;
import com.vivaahlok.vivahlok.dto.response.ReviewsResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendors", description = "Vendor APIs")
public class VendorController {
    
    private final VendorService vendorService;
    
    @GetMapping
    @Operation(summary = "Get all vendors with filters")
    public ResponseEntity<PageResponse<VendorListDTO>> getVendors(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        PageResponse<VendorListDTO> response = vendorService.getVendors(category, sort, minPrice, maxPrice, page, limit);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vendor details")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable String id) {
        VendorDTO vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendor);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get vendors by category")
    public ResponseEntity<List<VendorListDTO>> getVendorsByCategory(
            @PathVariable String category,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<VendorListDTO> vendors = vendorService.getVendorsByCategory(category, sort, minPrice, maxPrice);
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search vendors")
    public ResponseEntity<List<VendorListDTO>> searchVendors(
            @RequestParam String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<VendorListDTO> vendors = vendorService.searchVendors(q, category, minPrice, maxPrice);
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get vendor reviews")
    public ResponseEntity<ReviewsResponse> getVendorReviews(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        ReviewsResponse response = vendorService.getVendorReviews(id, page, limit);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/reviews")
    @Operation(summary = "Add review")
    public ResponseEntity<ApiResponse<Map<String, String>>> addReview(
            @PathVariable String id,
            @CurrentUser String userId,
            @Valid @RequestBody AddReviewRequest request) {
        String reviewId = vendorService.addReview(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Review added successfully", Map.of("reviewId", reviewId)));
    }
}
