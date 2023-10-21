package com.booking.roome.controller;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import com.booking.roome.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/facility")
public class facilityController {
    private final FacilityService facilityService;

    public facilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping
    public List<Facility> getAllFacilities() {
        return facilityService.facilities();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getFacility(@PathVariable int id) {
        return facilityService.getFacility(id);
    }

    @PostMapping
    public ResponseEntity<?> addFacility(@Valid @RequestPart("facility") facilityDto facility, @RequestPart("icon") MultipartFile icon) {
        return facilityService.addFacility(facility, icon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacility(@Valid @RequestPart("facility") facilityDto facilityDto, @RequestPart("icon") MultipartFile icon, @PathVariable int id) {
        return facilityService.updateFacility(facilityDto, icon, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacility(@PathVariable int id) {
        return facilityService.deleteFacility(id);
    }
}
