package com.vivaahlok.vivahlok.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private String id;
    private String vendorId;
    private String vendorName;
    private String image;
    private String vendorImage;
    private String vendorCategory;
    private Double vendorRating;
    private String category;
    private Double price;
    private LocalDateTime addedAt;
}
