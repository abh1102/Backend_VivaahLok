package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.*;
import com.vivaahlok.vivahlok.dto.request.AddReviewRequest;
import com.vivaahlok.vivahlok.dto.response.PageResponse;
import com.vivaahlok.vivahlok.dto.response.ReviewsResponse;
import com.vivaahlok.vivahlok.entity.Review;
import com.vivaahlok.vivahlok.entity.User;
import com.vivaahlok.vivahlok.entity.Vendor;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.ReviewRepository;
import com.vivaahlok.vivahlok.repository.UserRepository;
import com.vivaahlok.vivahlok.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorService {
    
    private final VendorRepository vendorRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    
    public PageResponse<VendorListDTO> getVendors(String category, String sort, Double minPrice, Double maxPrice, int page, int limit) {
        Pageable pageable = createPageable(page, limit, sort);
        Page<Vendor> vendorPage;
        
        if (category != null && !category.isEmpty()) {
            if (minPrice != null && maxPrice != null) {
                vendorPage = vendorRepository.findByCategoryAndIsActiveTrueAndPriceBetween(category, minPrice, maxPrice, pageable);
            } else {
                vendorPage = vendorRepository.findByCategoryAndIsActiveTrue(category, pageable);
            }
        } else {
            if (minPrice != null && maxPrice != null) {
                vendorPage = vendorRepository.findByIsActiveTrueAndPriceBetween(minPrice, maxPrice, pageable);
            } else {
                vendorPage = vendorRepository.findByIsActiveTrue(pageable);
            }
        }
        
        List<VendorListDTO> vendors = vendorPage.getContent().stream()
                .map(this::mapToVendorListDTO)
                .collect(Collectors.toList());
        
        return PageResponse.<VendorListDTO>builder()
                .content(vendors)
                .total(vendorPage.getTotalElements())
                .page(page)
                .pages(vendorPage.getTotalPages())
                .build();
    }
    
    public VendorDTO getVendorById(String id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        return mapToVendorDTO(vendor);
    }
    
    public List<VendorListDTO> getVendorsByCategory(String category, String sort, Double minPrice, Double maxPrice) {
        List<Vendor> vendors = vendorRepository.findByCategoryAndIsActiveTrue(category);
        
        if (minPrice != null && maxPrice != null) {
            vendors = vendors.stream()
                    .filter(v -> v.getPrice() >= minPrice && v.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        
        if (sort != null) {
            if (sort.equals("low_to_high") || sort.equals("price_asc")) {
                vendors.sort((a, b) -> Double.compare(a.getPrice() != null ? a.getPrice() : 0, b.getPrice() != null ? b.getPrice() : 0));
            } else if (sort.equals("high_to_low") || sort.equals("price_desc")) {
                vendors.sort((a, b) -> Double.compare(b.getPrice() != null ? b.getPrice() : 0, a.getPrice() != null ? a.getPrice() : 0));
            }
        }
        
        return vendors.stream()
                .map(this::mapToVendorListDTO)
                .collect(Collectors.toList());
    }
    
    public List<VendorListDTO> searchVendors(String query, String category, Double minPrice, Double maxPrice) {
        List<Vendor> vendors = vendorRepository.searchVendors(query);
        
        if (category != null && !category.isEmpty()) {
            vendors = vendors.stream()
                    .filter(v -> v.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        
        if (minPrice != null && maxPrice != null) {
            vendors = vendors.stream()
                    .filter(v -> v.getPrice() >= minPrice && v.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        
        return vendors.stream()
                .map(this::mapToVendorListDTO)
                .collect(Collectors.toList());
    }
    
    public ReviewsResponse getVendorReviews(String vendorId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.findByVendorId(vendorId, pageable);
        
        Double avgRating = reviewRepository.getAverageRatingByVendorId(vendorId);
        
        List<ReviewDTO> reviews = reviewPage.getContent().stream()
                .map(this::mapToReviewDTO)
                .collect(Collectors.toList());
        
        return ReviewsResponse.builder()
                .reviews(reviews)
                .avgRating(avgRating != null ? avgRating : 0.0)
                .total(reviewPage.getTotalElements())
                .page(page)
                .pages(reviewPage.getTotalPages())
                .build();
    }
    
    public String addReview(String vendorId, String userId, AddReviewRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (reviewRepository.findByUserIdAndVendorId(userId, vendorId).isPresent()) {
            throw new BadRequestException("You have already reviewed this vendor");
        }
        
        Review review = Review.builder()
                .userId(userId)
                .userName(user.getName())
                .vendorId(vendorId)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();
        
        review = reviewRepository.save(review);
        
        updateVendorRating(vendorId);
        
        return review.getId();
    }
    
    private void updateVendorRating(String vendorId) {
        Double avgRating = reviewRepository.getAverageRatingByVendorId(vendorId);
        long reviewCount = reviewRepository.countByVendorId(vendorId);
        
        Vendor vendor = vendorRepository.findById(vendorId).orElse(null);
        if (vendor != null) {
            vendor.setRating(avgRating);
            vendor.setReviewCount((int) reviewCount);
            vendorRepository.save(vendor);
        }
    }
    
    private Pageable createPageable(int page, int limit, String sort) {
        Sort sortOrder = Sort.unsorted();
        if (sort != null) {
            if (sort.equals("price_asc") || sort.equals("low_to_high")) {
                sortOrder = Sort.by(Sort.Direction.ASC, "price");
            } else if (sort.equals("price_desc") || sort.equals("high_to_low")) {
                sortOrder = Sort.by(Sort.Direction.DESC, "price");
            } else if (sort.equals("rating")) {
                sortOrder = Sort.by(Sort.Direction.DESC, "rating");
            }
        }
        return PageRequest.of(page - 1, limit, sortOrder);
    }
    
    private VendorListDTO mapToVendorListDTO(Vendor vendor) {
        return VendorListDTO.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .category(vendor.getCategory())
                .image(vendor.getImage())
                .price(vendor.getPrice())
                .rating(vendor.getRating())
                .services(vendor.getServices())
                .phone(vendor.getPhone())
                .build();
    }
    
    private VendorDTO mapToVendorDTO(Vendor vendor) {
        return VendorDTO.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .category(vendor.getCategory())
                .description(vendor.getDescription())
                .services(vendor.getServices())
                .price(vendor.getPrice())
                .rating(vendor.getRating())
                .reviewCount(vendor.getReviewCount())
                .phone(vendor.getPhone())
                .email(vendor.getEmail())
                .image(vendor.getImage())
                .gallery(vendor.getGallery())
                .address(vendor.getAddress())
                .city(vendor.getCity())
                .build();
    }
    
    private ReviewDTO mapToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .comment(review.getComment())
                .date(review.getCreatedAt())
                .build();
    }
}
