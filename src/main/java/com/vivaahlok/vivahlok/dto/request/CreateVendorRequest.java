package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateVendorRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private String description;
    
    private List<String> services;
    
    @NotNull(message = "Price is required")
    private Double price;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    private String email;
    
    private String image;
    
    private List<String> gallery;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "City is required")
    private String city;
    
    private Double latitude;
    
    private Double longitude;
    
    private Boolean isActive = true;
    
    private Boolean isFeatured = false;
}
