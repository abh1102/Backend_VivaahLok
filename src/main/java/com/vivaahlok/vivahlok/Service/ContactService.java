package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.ContactDTO;
import com.vivaahlok.vivahlok.dto.request.ContactVendorRequest;
import com.vivaahlok.vivahlok.dto.request.SyncContactsRequest;
import com.vivaahlok.vivahlok.entity.Contact;
import com.vivaahlok.vivahlok.entity.VendorInquiry;
import com.vivaahlok.vivahlok.repository.ContactRepository;
import com.vivaahlok.vivahlok.repository.VendorInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {
    
    private final ContactRepository contactRepository;
    private final VendorInquiryRepository vendorInquiryRepository;
    
    public void contactVendor(String userId, ContactVendorRequest request) {
        VendorInquiry inquiry = VendorInquiry.builder()
                .userId(userId)
                .vendorId(request.getVendorId())
                .type(request.getType())
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .build();
        
        vendorInquiryRepository.save(inquiry);
    }
    
    public List<ContactDTO> getUserContacts(String userId) {
        return contactRepository.findByUserId(userId).stream()
                .map(this::mapToContactDTO)
                .collect(Collectors.toList());
    }
    
    public int syncContacts(String userId, SyncContactsRequest request) {
        contactRepository.deleteByUserId(userId);
        
        List<Contact> contacts = request.getContacts().stream()
                .map(c -> Contact.builder()
                        .userId(userId)
                        .name(c.getName())
                        .phone(c.getPhone())
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        
        contactRepository.saveAll(contacts);
        return contacts.size();
    }
    
    private ContactDTO mapToContactDTO(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phone(contact.getPhone())
                .build();
    }
}
