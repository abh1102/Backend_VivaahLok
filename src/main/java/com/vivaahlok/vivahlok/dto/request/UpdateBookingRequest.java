package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookingRequest {
    private LocalDate date;
    private String time;
    private String notes;
}
