package com.vivaahlok.vivahlok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "service_types")
public class ServiceType {
    @Id
    private String id;
    private String name;
    private String icon;
    
    @Builder.Default
    private boolean isActive = true;
}
