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
@Document(collection = "occasions")
public class Occasion {
    @Id
    private String id;
    private String name;
    private String image;
    private String description;
    
    @Builder.Default
    private boolean isActive = true;
    
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
