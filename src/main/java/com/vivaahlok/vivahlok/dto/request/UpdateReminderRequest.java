package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateReminderRequest {
    private String eventName;
    private LocalDate date;
    private String time;
    private String message;
    private Boolean pushEnabled;
    private Boolean whatsAppEnabled;
}
