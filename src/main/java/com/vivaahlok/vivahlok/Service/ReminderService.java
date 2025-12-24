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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {
    
    private final ReminderRepository reminderRepository;
    
    public List<ReminderDTO> getUserReminders(String userId) {
        return reminderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToReminderDTO)
                .collect(Collectors.toList());
    }
    
    public ReminderDTO getReminderById(String reminderId, String userId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        
        if (!reminder.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to reminder");
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
                .pushEnabled(request.isPushEnabled())
                .whatsAppEnabled(request.isWhatsAppEnabled())
                .contacts(request.getContacts())
                .createdAt(LocalDateTime.now())
                .build();
        
        reminder = reminderRepository.save(reminder);
        return reminder.getId();
    }
    
    public void updateReminder(String reminderId, String userId, UpdateReminderRequest request) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        
        if (!reminder.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to reminder");
        }
        
        if (request.getEventName() != null) {
            reminder.setEventName(request.getEventName());
        }
        if (request.getDate() != null) {
            reminder.setDate(request.getDate());
        }
        if (request.getTime() != null) {
            reminder.setTime(request.getTime());
        }
        if (request.getMessage() != null) {
            reminder.setMessage(request.getMessage());
        }
        if (request.getPushEnabled() != null) {
            reminder.setPushEnabled(request.getPushEnabled());
        }
        if (request.getWhatsAppEnabled() != null) {
            reminder.setWhatsAppEnabled(request.getWhatsAppEnabled());
        }
        
        reminder.setUpdatedAt(LocalDateTime.now());
        reminderRepository.save(reminder);
    }
    
    public void deleteReminder(String reminderId, String userId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        
        if (!reminder.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to reminder");
        }
        
        reminderRepository.delete(reminder);
    }
    
    @Transactional
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
                .build();
    }
}
