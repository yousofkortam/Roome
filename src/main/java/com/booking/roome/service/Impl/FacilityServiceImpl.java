package com.booking.roome.service.Impl;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.exception.ExceptionRequest;
import com.booking.roome.model.Facility;
import com.booking.roome.repository.FacilityRepository;
import com.booking.roome.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Override
    public List<Facility> facilities() {
        return facilityRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getFacility(int id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Facility not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.status(HttpStatus.OK).body(facility);
    }

    @Override
    public ResponseEntity<?> addFacility(facilityDto facility) {
        Facility newFacility = new Facility();
        newFacility.setName(facility.getName());

        try {
            facilityRepository.save(newFacility);
        }catch (RuntimeException e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(facility);
    }

    @Override
    public ResponseEntity<?> updateFacility(facilityDto updatedFacility, int id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Facility not found", HttpStatus.NOT_FOUND));

        facility.setName(updatedFacility.getName());

        try {
            facilityRepository.save(facility);
        }catch (RuntimeException e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(facility);
    }

    @Override
    public ResponseEntity<?> deleteFacility(int id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Facility not found", HttpStatus.NOT_FOUND));

        try {
            facilityRepository.delete(facility);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new ExceptionRequest("Facility deleted successfully", HttpStatus.OK.value()));
    }
}
