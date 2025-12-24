package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.CityDTO;
import com.vivaahlok.vivahlok.dto.OccasionDTO;
import com.vivaahlok.vivahlok.dto.ServiceTypeDTO;
import com.vivaahlok.vivahlok.service.MasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
@Tag(name = "Master Data", description = "Master Data APIs")
public class MasterDataController {
    
    private final MasterDataService masterDataService;
    
    @GetMapping("/cities")
    @Operation(summary = "Get list of cities")
    public ResponseEntity<List<CityDTO>> getCities() {
        List<CityDTO> cities = masterDataService.getCities();
        return ResponseEntity.ok(cities);
    }
    
    @GetMapping("/service-types")
    @Operation(summary = "Get service types")
    public ResponseEntity<List<ServiceTypeDTO>> getServiceTypes() {
        List<ServiceTypeDTO> serviceTypes = masterDataService.getServiceTypes();
        return ResponseEntity.ok(serviceTypes);
    }
    
    @GetMapping("/occasion-types")
    @Operation(summary = "Get occasion types")
    public ResponseEntity<List<OccasionDTO>> getOccasionTypes() {
        List<OccasionDTO> occasions = masterDataService.getOccasionTypes();
        return ResponseEntity.ok(occasions);
    }
}
