package com.booking.roome.service;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FacilityService {
    List<Facility> facilities();
    ResponseEntity<?> getFacility(int id);
    ResponseEntity<?> addFacility(facilityDto facility, MultipartFile icon);
    ResponseEntity<?> updateFacility(facilityDto facilityDto, MultipartFile icon, int id);
    ResponseEntity<?> deleteFacility(int id);
}
