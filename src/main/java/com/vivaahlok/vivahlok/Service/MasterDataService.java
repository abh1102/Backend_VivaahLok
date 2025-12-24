package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.CityDTO;
import com.vivaahlok.vivahlok.dto.OccasionDTO;
import com.vivaahlok.vivahlok.dto.ServiceTypeDTO;
import com.vivaahlok.vivahlok.entity.City;
import com.vivaahlok.vivahlok.entity.Occasion;
import com.vivaahlok.vivahlok.entity.ServiceType;
import com.vivaahlok.vivahlok.repository.CityRepository;
import com.vivaahlok.vivahlok.repository.OccasionRepository;
import com.vivaahlok.vivahlok.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterDataService {
    
    private final CityRepository cityRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final OccasionRepository occasionRepository;
    
    public List<CityDTO> getCities() {
        return cityRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToCityDTO)
                .collect(Collectors.toList());
    }
    
    public List<ServiceTypeDTO> getServiceTypes() {
        return serviceTypeRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToServiceTypeDTO)
                .collect(Collectors.toList());
    }
    
    public List<OccasionDTO> getOccasionTypes() {
        return occasionRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(this::mapToOccasionDTO)
                .collect(Collectors.toList());
    }
    
    private CityDTO mapToCityDTO(City city) {
        return CityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .state(city.getState())
                .build();
    }
    
    private ServiceTypeDTO mapToServiceTypeDTO(ServiceType serviceType) {
        return ServiceTypeDTO.builder()
                .id(serviceType.getId())
                .name(serviceType.getName())
                .icon(serviceType.getIcon())
                .build();
    }
    
    private OccasionDTO mapToOccasionDTO(Occasion occasion) {
        return OccasionDTO.builder()
                .id(occasion.getId())
                .name(occasion.getName())
                .image(occasion.getImage())
                .description(occasion.getDescription())
                .build();
    }
}
