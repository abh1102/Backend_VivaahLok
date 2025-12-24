package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.FacilityDTO;
import com.vivaahlok.vivahlok.entity.Facility;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.FacilityRepository;
import com.vivaahlok.vivahlok.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    
    private final FacilityRepository facilityRepository;
    private final VendorRepository vendorRepository;
    
    public List<FacilityDTO> getAllFacilities() {
        return facilityRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToFacilityDTO)
                .collect(Collectors.toList());
    }
    
    public FacilityDTO getFacilityById(String id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));
        
        FacilityDTO dto = mapToFacilityDTO(facility);
        long vendorCount = vendorRepository.countByCategoryAndIsActiveTrue(facility.getName());
        dto.setVendorCount((int) vendorCount);
        
        return dto;
    }
    
    private FacilityDTO mapToFacilityDTO(Facility facility) {
        return FacilityDTO.builder()
                .id(facility.getId())
                .name(facility.getName())
                .icon(facility.getIcon())
                .image(facility.getImage())
                .description(facility.getDescription())
                .build();
    }
}
