package com.booking.roome.controller;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import com.booking.roome.service.facility.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("facilities")
public class facilityController {

    private final FacilityService facilityService;

    @GetMapping
    public List<Facility> getAllFacilities() {
        return facilityService.facilities();
    }


    @GetMapping("{id}")
    public Facility getFacility(@PathVariable int id) {
        return facilityService.getFacility(id);
    }

    @PostMapping
    public Facility addFacility(@Valid @RequestPart("facility") facilityDto facility, @RequestPart("icon") MultipartFile icon) {
        return facilityService.addFacility(facility, icon);
    }

    @PutMapping("{id}")
    public Facility updateFacility(@Valid @RequestPart("facility") facilityDto facilityDto, @RequestPart("icon") MultipartFile icon, @PathVariable int id) {
        return facilityService.updateFacility(facilityDto, icon, id);
    }

    @DeleteMapping("{id}")
    public void deleteFacility(@PathVariable int id) {
        facilityService.deleteFacility(id);
    }
}
