package com.vivaahlok.vivahlok.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private String id;
    private String vendorId;
    private String vendorName;
    private LocalDate date;
    private String time;
    private String status;
    private Double amount;
    private List<String> services;
    private String notes;
}
