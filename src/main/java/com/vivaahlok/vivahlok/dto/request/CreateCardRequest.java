package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCardRequest {
    @NotBlank(message = "Design ID is required")
    private String designId;
    
    @NotBlank(message = "Groom name is required")
    private String groomName;
    
    @NotBlank(message = "Bride name is required")
    private String brideName;
    
    @NotBlank(message = "Event date is required")
    private String eventDate;
    
    @NotBlank(message = "Venue is required")
    private String venue;
    
    private boolean isFamily = false;
}
