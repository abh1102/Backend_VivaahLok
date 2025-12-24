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
@Document(collection = "invitation_cards")
public class InvitationCard {
    @Id
    private String id;
    private String userId;
    private String designId;
    private String designName;
    private String groomName;
    private String brideName;
    private String eventDate;
    private String venue;
    
    @Builder.Default
    private boolean isFamily = false;
    
    private String cardUrl;
    private String shareUrl;
    private String thumbnail;
    private LocalDateTime createdAt;
}
