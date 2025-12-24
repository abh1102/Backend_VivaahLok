package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateBookingRequest {
    @NotBlank(message = "Vendor ID is required")
    private String vendorId;
    
    @NotNull(message = "Event date is required")
    private LocalDate date;
    
    @NotBlank(message = "Event time is required")
    private String time;
    
    private List<String> services;
    
    private String notes;
}
