package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.*;
import com.vivaahlok.vivahlok.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@Tag(name = "Home & Discovery", description = "Home page and discovery APIs")
public class HomeController {
    
    private final HomeService homeService;
    
    @GetMapping("/banners")
    @Operation(summary = "Get promotional banners")
    public ResponseEntity<List<BannerDTO>> getBanners() {
        List<BannerDTO> banners = homeService.getBanners();
        return ResponseEntity.ok(banners);
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get all service categories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = homeService.getCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/occasions")
    @Operation(summary = "Get all occasion types")
    public ResponseEntity<List<OccasionDTO>> getOccasions() {
        List<OccasionDTO> occasions = homeService.getOccasions();
        return ResponseEntity.ok(occasions);
    }
    
    @GetMapping("/nearby")
    @Operation(summary = "Get nearby services")
    public ResponseEntity<List<NearbyServiceDTO>> getNearbyServices(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "10") Integer radius) {
        List<NearbyServiceDTO> nearbyServices = homeService.getNearbyServices(lat, lng, radius);
        return ResponseEntity.ok(nearbyServices);
    }
    
    @GetMapping("/featured")
    @Operation(summary = "Get featured vendors")
    public ResponseEntity<List<FeaturedVendorDTO>> getFeaturedVendors() {
        List<FeaturedVendorDTO> featuredVendors = homeService.getFeaturedVendors();
        return ResponseEntity.ok(featuredVendors);
    }
}
