package com.booking.roome.controller;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import com.booking.roome.service.FacilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> AddFacility(@RequestBody facilityDto facility) {
        return facilityService.addFacility(facility);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacility(@RequestBody facilityDto facilityDto, @PathVariable int id) {
        return facilityService.updateFacility(facilityDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacility(@PathVariable int id) {
        return facilityService.deleteFacility(id);
    }
}
