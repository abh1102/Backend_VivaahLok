package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.*;
import com.vivaahlok.vivahlok.entity.Banner;
import com.vivaahlok.vivahlok.entity.Category;
import com.vivaahlok.vivahlok.entity.Occasion;
import com.vivaahlok.vivahlok.entity.Vendor;
import com.vivaahlok.vivahlok.repository.BannerRepository;
import com.vivaahlok.vivahlok.repository.CategoryRepository;
import com.vivaahlok.vivahlok.repository.OccasionRepository;
import com.vivaahlok.vivahlok.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {
    
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final OccasionRepository occasionRepository;
    private final VendorRepository vendorRepository;
    
    public List<BannerDTO> getBanners() {
        return bannerRepository.findByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(this::mapToBannerDTO)
                .collect(Collectors.toList());
    }
    
    public List<CategoryDTO> getCategories() {
        return categoryRepository.findByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
    }
    
    public List<OccasionDTO> getOccasions() {
        return occasionRepository.findByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(this::mapToOccasionDTO)
                .collect(Collectors.toList());
    }
    
    public List<NearbyServiceDTO> getNearbyServices(Double lat, Double lng, Integer radius) {
        List<Vendor> vendors = vendorRepository.findByIsActiveTrue();
        
        return vendors.stream()
                .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
                .filter(v -> calculateDistance(lat, lng, v.getLatitude(), v.getLongitude()) <= radius)
                .map(v -> NearbyServiceDTO.builder()
                        .id(v.getId())
                        .name(v.getName())
                        .category(v.getCategory())
                        .image(v.getImage())
                        .distance(calculateDistance(lat, lng, v.getLatitude(), v.getLongitude()))
                        .rating(v.getRating())
                        .build())
                .collect(Collectors.toList());
    }
    
    public List<FeaturedVendorDTO> getFeaturedVendors() {
        return vendorRepository.findByIsFeaturedTrueAndIsActiveTrue().stream()
                .map(v -> FeaturedVendorDTO.builder()
                        .id(v.getId())
                        .name(v.getName())
                        .category(v.getCategory())
                        .image(v.getImage())
                        .rating(v.getRating())
                        .price(v.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    private BannerDTO mapToBannerDTO(Banner banner) {
        return BannerDTO.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .subtitle(banner.getSubtitle())
                .image(banner.getImage())
                .actionUrl(banner.getActionUrl())
                .build();
    }
    
    private CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .icon(category.getIcon())
                .build();
    }
    
    private OccasionDTO mapToOccasionDTO(Occasion occasion) {
        return OccasionDTO.builder()
                .id(occasion.getId())
                .name(occasion.getName())
                .image(occasion.getImage())
                .description(occasion.getDescription())
                .build();
    }
}
