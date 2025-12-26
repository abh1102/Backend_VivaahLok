package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateVendorRequest {
    
    private String name;
    
    private String category;
    
    private String description;
    
    private List<String> services;
    
    private Double price;
    
    private String phone;
    
    private String email;
    
    private String image;
    
    private List<String> gallery;
    
    private String address;
    
    private String city;
    
    private Double latitude;
    
    private Double longitude;
    
    private Boolean isActive;
    
    private Boolean isFeatured;
}
