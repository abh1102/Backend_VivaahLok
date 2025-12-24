package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.SettingsDTO;
import com.vivaahlok.vivahlok.dto.request.UpdateLocationRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateSettingsRequest;
import com.vivaahlok.vivahlok.entity.UserSettings;
import com.vivaahlok.vivahlok.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SettingsService {
    
    private final UserSettingsRepository userSettingsRepository;
    
    public SettingsDTO getSettings(String userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
        
        return mapToSettingsDTO(settings);
    }
    
    public void updateSettings(String userId, UpdateSettingsRequest request) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
        
        if (request.getNotifications() != null) {
            settings.setNotifications(request.getNotifications());
        }
        if (request.getLanguage() != null) {
            settings.setLanguage(request.getLanguage());
        }
        
        settings.setUpdatedAt(LocalDateTime.now());
        userSettingsRepository.save(settings);
    }
    
    public void updateLocation(String userId, UpdateLocationRequest request) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
        
        if (request.getCity() != null) {
            settings.setCity(request.getCity());
        }
        if (request.getLat() != null) {
            settings.setLatitude(request.getLat());
        }
        if (request.getLng() != null) {
            settings.setLongitude(request.getLng());
        }
        
        settings.setUpdatedAt(LocalDateTime.now());
        userSettingsRepository.save(settings);
    }
    
    private UserSettings createDefaultSettings(String userId) {
        UserSettings settings = UserSettings.builder()
                .userId(userId)
                .notifications(true)
                .language("en")
                .build();
        return userSettingsRepository.save(settings);
    }
    
    private SettingsDTO mapToSettingsDTO(UserSettings settings) {
        return SettingsDTO.builder()
                .notifications(settings.isNotifications())
                .language(settings.getLanguage())
                .location(settings.getCity())
                .build();
    }
}
