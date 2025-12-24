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
@Document(collection = "facilities")
public class Facility {
    @Id
    private String id;
    private String name;
    private String icon;
    private String image;
    private String description;
    
    @Builder.Default
    private boolean isActive = true;
    
    private LocalDateTime createdAt;
}
