package com.vivaahlok.vivahlok.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddWishlistRequest {
    @NotBlank(message = "Vendor ID is required")
    private String vendorId;
}
