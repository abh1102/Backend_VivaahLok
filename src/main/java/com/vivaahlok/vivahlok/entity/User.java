package com.vivaahlok.vivahlok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    
    @Indexed(unique = true)
    private String phone;
    
    @Indexed(unique = true)
    private String firebaseUid;
    
    private String password;
    private String address;
    private String city;
    private String occupation;
    private String profileImage;
    
    @Builder.Default
    private String role = "USER";
    
    @Builder.Default
    private boolean isVerified = false;
    
    private String fcmToken;
    private String platform;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
