package com.vivaahlok.vivahlok.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationCardDTO {
    private String id;
    private String designId;
    private String designName;
    private String groomName;
    private String brideName;
    private String eventDate;
    private String venue;
    private boolean isFamily;
    private String cardUrl;
    private String shareUrl;
    private String thumbnail;
    private LocalDateTime createdAt;
}
