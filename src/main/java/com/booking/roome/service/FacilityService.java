package com.booking.roome.service;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FacilityService {
    List<Facility> facilities();
    ResponseEntity<?> getFacility(int id);
    ResponseEntity<?> addFacility(facilityDto facility);
    ResponseEntity<?> updateFacility(facilityDto facilityDto, int id);
    ResponseEntity<?> deleteFacility(int id);
}
