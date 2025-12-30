package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookingRequest {
    private LocalDate eventDate;
    private String eventTime;
    private String notes;
}
