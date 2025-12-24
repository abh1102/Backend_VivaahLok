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
public class ReminderDTO {
    private String id;
    private String eventName;
    private LocalDate date;
    private String time;
    private String message;
    private boolean pushEnabled;
    private boolean whatsAppEnabled;
    private List<String> contacts;
}
