package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.NotificationDTO;
import com.vivaahlok.vivahlok.dto.request.RegisterDeviceRequest;
import com.vivaahlok.vivahlok.dto.response.NotificationsResponse;
import com.vivaahlok.vivahlok.entity.Notification;
import com.vivaahlok.vivahlok.entity.User;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.NotificationRepository;
import com.vivaahlok.vivahlok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    
    public void registerDevice(String userId, RegisterDeviceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setFcmToken(request.getFcmToken());
        user.setPlatform(request.getPlatform());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public NotificationsResponse getNotifications(String userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Notification> notificationPage = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        long unreadCount = notificationRepository.countByUserIdAndIsReadFalse(userId);
        
        List<NotificationDTO> notifications = notificationPage.getContent().stream()
                .map(this::mapToNotificationDTO)
                .collect(Collectors.toList());
        
        return NotificationsResponse.builder()
                .notifications(notifications)
                .unreadCount(unreadCount)
                .build();
    }
    
    public void markAsRead(String notificationId, String userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        
        unreadNotifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
    
    private NotificationDTO mapToNotificationDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .actionUrl(notification.getActionUrl())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
