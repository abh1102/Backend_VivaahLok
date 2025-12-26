package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.WishlistDTO;
import com.vivaahlok.vivahlok.entity.Vendor;
import com.vivaahlok.vivahlok.entity.Wishlist;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.VendorRepository;
import com.vivaahlok.vivahlok.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    
    private final WishlistRepository wishlistRepository;
    private final VendorRepository vendorRepository;
    
    public List<WishlistDTO> getUserWishlist(String userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        
        return wishlists.stream()
                .map(wishlist -> {
                    Vendor vendor = vendorRepository.findById(wishlist.getVendorId()).orElse(null);
                    if (vendor == null) return null;
                    
                    return WishlistDTO.builder()
                            .id(wishlist.getId())
                            .vendorId(vendor.getId())
                            .vendorName(vendor.getName())
                            .vendorImage(vendor.getImage())
                            .vendorCategory(vendor.getCategory())
                            .vendorRating(vendor.getRating())
                            .addedAt(wishlist.getCreatedAt())
                            .build();
                })
                .filter(w -> w != null)
                .collect(Collectors.toList());
    }
    
    public String addToWishlist(String userId, String vendorId) {
        if (wishlistRepository.existsByUserIdAndVendorId(userId, vendorId)) {
            throw new BadRequestException("Vendor already in wishlist");
        }
        
        vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .vendorId(vendorId)
                .createdAt(LocalDateTime.now())
                .build();
        
        wishlist = wishlistRepository.save(wishlist);
        return wishlist.getId();
    }
    
    public void removeFromWishlist(String userId, String vendorId) {
        wishlistRepository.deleteByUserIdAndVendorId(userId, vendorId);
    }
}
