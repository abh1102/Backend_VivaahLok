package com.vivaahlok.vivahlok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vendors")
public class Vendor {
    @Id
    private String id;
    private String name;
    private String category;
    private String description;
    private List<String> services;
    private Double price;
    private Double rating;
    private Integer reviewCount;
    private String phone;
    private String email;
    private String image;
    private List<String> gallery;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    @Builder.Default
    private boolean isActive = true;
    
    @Builder.Default
    private boolean isFeatured = false;

    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
