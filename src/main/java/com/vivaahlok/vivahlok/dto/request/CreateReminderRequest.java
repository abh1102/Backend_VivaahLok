package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateReminderRequest {
    @NotBlank(message = "Event name is required")
    private String eventName;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotBlank(message = "Time is required")
    private String time;
    
    private String message;
    private boolean pushEnabled = true;
    private boolean whatsAppEnabled = false;
    private List<String> contacts;
}
