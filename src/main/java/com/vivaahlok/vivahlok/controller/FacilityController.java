package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.FacilityDTO;
import com.vivaahlok.vivahlok.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
@Tag(name = "Facilities", description = "Facilities/Services APIs")
public class FacilityController {
    
    private final FacilityService facilityService;
    
    @GetMapping
    @Operation(summary = "Get all facilities/services")
    public ResponseEntity<List<FacilityDTO>> getAllFacilities() {
        List<FacilityDTO> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get facility details")
    public ResponseEntity<FacilityDTO> getFacilityById(@PathVariable String id) {
        FacilityDTO facility = facilityService.getFacilityById(id);
        return ResponseEntity.ok(facility);
    }
}
