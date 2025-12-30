package com.vivaahlok.vivahlok.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private String id;
    private String vendorId;
    private String vendorName;
    private LocalDate eventDate;
    private String eventTime;
    private String status;
    private Double amount;
    private List<String> services;
    private String notes;
    private LocalDateTime createdAt;
}
