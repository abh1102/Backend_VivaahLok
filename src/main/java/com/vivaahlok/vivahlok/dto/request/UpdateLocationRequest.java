package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

@Data
public class UpdateLocationRequest {
    private String city;
    private Double lat;
    private Double lng;
}
