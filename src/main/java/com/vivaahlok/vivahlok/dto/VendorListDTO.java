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
public class VendorListDTO {
    private String id;
    private String name;
    private String category;
    private String image;
    private Double price;
    private Double rating;
    private List<String> services;
    private String phone;
}
