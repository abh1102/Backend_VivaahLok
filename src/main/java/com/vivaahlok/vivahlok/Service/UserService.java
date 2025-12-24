package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.UserDTO;
import com.vivaahlok.vivahlok.dto.request.UpdateProfileRequest;
import com.vivaahlok.vivahlok.entity.User;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    
    public UserDTO getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserDTO(user);
    }
    
    public UserDTO updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getOccupation() != null) {
            user.setOccupation(request.getOccupation());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        
        return mapToUserDTO(user);
    }
    
    public String uploadProfileImage(String userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String imageUrl = fileStorageService.uploadFile(file, "profiles/" + userId);
        
        user.setProfileImage(imageUrl);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        return imageUrl;
    }
    
    public void deleteProfileImage(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getProfileImage() != null) {
            fileStorageService.deleteFile(user.getProfileImage());
            user.setProfileImage(null);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .occupation(user.getOccupation())
                .profileImage(user.getProfileImage())
                .build();
    }
}
