package com.booking.roome.service.Impl;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.exception.ExceptionRequest;
import com.booking.roome.model.Facility;
import com.booking.roome.repository.FacilityRepository;
import com.booking.roome.service.FacilityService;
import com.booking.roome.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final UploadFileService uploadFileService;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository, UploadFileService uploadFileService) {
        this.facilityRepository = facilityRepository;
        this.uploadFileService = uploadFileService;
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
    public ResponseEntity<?> addFacility(facilityDto facility, MultipartFile icon) {
        Facility newFacility = new Facility();
        newFacility.setName(facility.getName());
        String iconName = icon.isEmpty() ? null : uploadFileService.uploadFile(icon);
        newFacility.setIcon(iconName);

        try {
            facilityRepository.save(newFacility);
        }catch (RuntimeException e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(facility);
    }

    private String uploadIcon(MultipartFile file) {
        String UPLOADED_FOLDER = "src/main/resources/static/icons/";

        try {

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : null;
            String filename = (originalFilename != null ? originalFilename.substring(0, originalFilename.lastIndexOf('.')) : null) + new Date().getTime() + extension;

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + filename);
            Files.write(path, bytes);

            return filename;

        } catch (Exception e) {
            throw new ExceptionResponse("Failed to upload icon", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> updateFacility(facilityDto updatedFacility, MultipartFile icon, int id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Facility not found", HttpStatus.NOT_FOUND));

        facility.setName(updatedFacility.getName());
        String iconName = icon.isEmpty() ? null : uploadIcon(icon);
        facility.setIcon(iconName);

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
