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
@Document(collection = "vendor_inquiries")
public class VendorInquiry {
    @Id
    private String id;
    private String userId;
    private String vendorId;
    private String type; // call, whatsapp
    private String message;
    private LocalDateTime createdAt;
}
