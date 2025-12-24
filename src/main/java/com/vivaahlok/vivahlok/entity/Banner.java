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
@Document(collection = "banners")
public class Banner {
    @Id
    private String id;
    private String title;
    private String subtitle;
    private String image;
    private String actionUrl;
    
    @Builder.Default
    private boolean isActive = true;
    
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
