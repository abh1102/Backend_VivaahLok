package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.ReminderDTO;
import com.vivaahlok.vivahlok.dto.request.CreateReminderRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateReminderRequest;
import com.vivaahlok.vivahlok.entity.Reminder;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {
    
    private final ReminderRepository reminderRepository;
    
    public List<ReminderDTO> getUserReminders(String userId) {
        return reminderRepository.findByUserIdOrderByDateAsc(userId).stream()
                .map(this::mapToReminderDTO)
                .collect(Collectors.toList());
    }
    
    public ReminderDTO getReminderById(String id, String userId) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        
        if (!reminder.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        return mapToReminderDTO(reminder);
    }
    
    public String createReminder(String userId, CreateReminderRequest request) {
        Reminder reminder = Reminder.builder()
                .userId(userId)
                .eventName(request.getEventName())
                .date(request.getDate())
                .time(request.getTime())
                .message(request.getMessage())
                .pushEnabled(request.getPushEnabled() != null ? request.getPushEnabled() : true)
                .whatsAppEnabled(request.getWhatsAppEnabled() != null ? request.getWhatsAppEnabled() : false)
                .contacts(request.getContacts())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        reminder = reminderRepository.save(reminder);
        return reminder.getId();
    }
    
    public void updateReminder(String id, String userId, UpdateReminderRequest request) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        
        if (!reminder.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        if (request.getEventName() != null) reminder.setEventName(request.getEventName());
        if (request.getDate() != null) reminder.setDate(request.getDate());
        if (request.getTime() != null) reminder.setTime(request.getTime());
        if (request.getMessage() != null) reminder.setMessage(request.getMessage());
        if (request.getPushEnabled() != null) reminder.setPushEnabled(request.getPushEnabled());
        if (request.getWhatsAppEnabled() != null) reminder.setWhatsAppEnabled(request.getWhatsAppEnabled());
        if (request.getContacts() != null) reminder.setContacts(request.getContacts());
        
        reminder.setUpdatedAt(LocalDateTime.now());
        reminderRepository.save(reminder);
    }
    
    public void deleteReminder(String id, String userId) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        
        if (!reminder.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        reminderRepository.delete(reminder);
    }
    
    public void deleteAllReminders(String userId) {
        reminderRepository.deleteByUserId(userId);
    }
    
    private ReminderDTO mapToReminderDTO(Reminder reminder) {
        return ReminderDTO.builder()
                .id(reminder.getId())
                .eventName(reminder.getEventName())
                .date(reminder.getDate())
                .time(reminder.getTime())
                .message(reminder.getMessage())
                .pushEnabled(reminder.isPushEnabled())
                .whatsAppEnabled(reminder.isWhatsAppEnabled())
                .contacts(reminder.getContacts())
                .createdAt(reminder.getCreatedAt())
                .build();
    }
}
