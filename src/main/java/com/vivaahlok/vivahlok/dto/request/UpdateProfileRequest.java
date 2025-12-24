package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String address;
    private String city;
    private String occupation;
}
