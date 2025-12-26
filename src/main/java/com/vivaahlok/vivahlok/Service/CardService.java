package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.CardDesignDTO;
import com.vivaahlok.vivahlok.dto.InvitationCardDTO;
import com.vivaahlok.vivahlok.dto.request.CreateCardRequest;
import com.vivaahlok.vivahlok.dto.response.ShareResponse;
import com.vivaahlok.vivahlok.entity.CardDesign;
import com.vivaahlok.vivahlok.entity.InvitationCard;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.CardDesignRepository;
import com.vivaahlok.vivahlok.repository.InvitationCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    
    private final CardDesignRepository cardDesignRepository;
    private final InvitationCardRepository invitationCardRepository;
    
    public List<CardDesignDTO> getCardDesigns() {
        return cardDesignRepository.findByIsActiveTrue().stream()
                .map(this::mapToCardDesignDTO)
                .collect(Collectors.toList());
    }
    
    public String createCard(String userId, CreateCardRequest request) {
        CardDesign design = cardDesignRepository.findById(request.getDesignId())
                .orElseThrow(() -> new ResourceNotFoundException("Card design not found"));
        
        String shareToken = UUID.randomUUID().toString();
        
        InvitationCard card = InvitationCard.builder()
                .userId(userId)
                .designId(request.getDesignId())
                .designName(design.getName())
                .groomName(request.getGroomName())
                .brideName(request.getBrideName())
                .eventDate(request.getEventDate())
                .venue(request.getVenue())
                .isFamily(request.getIsFamily() != null ? request.getIsFamily() : false)
                .cardUrl("/cards/view/" + shareToken)
                .shareUrl("/cards/share/" + shareToken)
                .thumbnail(design.getPreviewImage())
                .createdAt(LocalDateTime.now())
                .build();
        
        card = invitationCardRepository.save(card);
        return card.getId();
    }
    
    public List<InvitationCardDTO> getUserCards(String userId) {
        return invitationCardRepository.findByUserId(userId).stream()
                .map(this::mapToInvitationCardDTO)
                .collect(Collectors.toList());
    }
    
    public InvitationCardDTO getCardById(String id, String userId) {
        InvitationCard card = invitationCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        
        if (!card.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        return mapToInvitationCardDTO(card);
    }
    
    public void deleteCard(String id, String userId) {
        InvitationCard card = invitationCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        
        if (!card.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        invitationCardRepository.delete(card);
    }
    
    public ShareResponse getShareLink(String id, String userId) {
        InvitationCard card = invitationCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        
        if (!card.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        String baseUrl = "https://vivaahlok.com";
        String shareUrl = baseUrl + card.getShareUrl();
        String whatsappUrl = "https://wa.me/?text=" + java.net.URLEncoder.encode(
                "You're invited! " + shareUrl, java.nio.charset.StandardCharsets.UTF_8);
        
        return ShareResponse.builder()
                .shareUrl(shareUrl)
                .whatsappUrl(whatsappUrl)
                .build();
    }
    
    private CardDesignDTO mapToCardDesignDTO(CardDesign design) {
        return CardDesignDTO.builder()
                .id(design.getId())
                .name(design.getName())
                .previewImage(design.getPreviewImage())
                .category(design.getCategory())
                .templateUrl(design.getTemplateUrl())
                .build();
    }
    
    private InvitationCardDTO mapToInvitationCardDTO(InvitationCard card) {
        return InvitationCardDTO.builder()
                .id(card.getId())
                .designId(card.getDesignId())
                .designName(card.getDesignName())
                .groomName(card.getGroomName())
                .brideName(card.getBrideName())
                .eventDate(card.getEventDate())
                .venue(card.getVenue())
                .isFamily(card.isFamily())
                .cardUrl(card.getCardUrl())
                .shareUrl(card.getShareUrl())
                .thumbnail(card.getThumbnail())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
