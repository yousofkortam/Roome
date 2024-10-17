package com.booking.roome.service.facility;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FacilityService {
    List<Facility> facilities();
    List<Facility> findAllById(List<Integer> ids);
    Facility getFacility(int id);
    Facility addFacility(facilityDto facility, MultipartFile icon);
    Facility updateFacility(facilityDto facilityDto, MultipartFile icon, int id);
    void deleteFacility(int id);
}
