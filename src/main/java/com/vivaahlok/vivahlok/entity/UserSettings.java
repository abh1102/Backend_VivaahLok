package com.vivaahlok.vivahlok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_settings")
public class UserSettings {
    @Id
    private String id;
    private String userId;
    
    @Builder.Default
    private boolean notifications = true;
    
    @Builder.Default
    private String language = "en";
    
    private String city;
    private Double latitude;
    private Double longitude;
    private LocalDateTime updatedAt;
}
