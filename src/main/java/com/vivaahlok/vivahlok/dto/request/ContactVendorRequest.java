package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactVendorRequest {
    @NotBlank(message = "Vendor ID is required")
    private String vendorId;
    
    @NotBlank(message = "Contact type is required")
    private String type; // call, whatsapp
    
    private String message;
}
