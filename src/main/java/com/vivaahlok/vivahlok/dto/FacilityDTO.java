package com.vivaahlok.vivahlok.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {
    private String id;
    private String name;
    private String icon;
    private String image;
    private String description;
    private Integer vendorCount;
}
