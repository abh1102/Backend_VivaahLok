package com.vivaahlok.vivahlok.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDTO {
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
}
