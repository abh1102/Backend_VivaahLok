package com.vivaahlok.vivahlok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reminders")
public class Reminder {
    @Id
    private String id;
    private String userId;
    private String eventName;
    private LocalDate date;
    private String time;
    private String message;
    
    @Builder.Default
    private boolean pushEnabled = true;
    
    @Builder.Default
    private boolean whatsAppEnabled = false;
    
    private List<String> contacts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
