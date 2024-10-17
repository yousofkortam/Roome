package com.booking.roome.service.facility;

import com.booking.roome.dto.facilityDto;
import com.booking.roome.model.Facility;
import com.booking.roome.repository.FacilityRepository;
import com.booking.roome.service.uploadFile.UploadFileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final UploadFileService uploadFileService;

    @Override
    public List<Facility> facilities() {
        return facilityRepository.findAll();
    }

    @Override
    public List<Facility> findAllById(List<Integer> ids) {
        return facilityRepository.findAllById(ids);
    }

    @Override
    public Facility getFacility(int id) {
        return facilityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Facility not found")
        );
    }

    @Override
    public Facility addFacility(facilityDto facility, MultipartFile icon) {
        return facilityRepository.save(Facility.builder()
                .name(facility.getName())
                        .icon(icon.isEmpty() ? null : uploadFileService.uploadFile(icon))
                .build());
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
            throw new RuntimeException("Failed to upload icon");
        }
    }

    @Override
    public Facility updateFacility(facilityDto updatedFacility, MultipartFile icon, int id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Facility not found")
        );
        facility.setName(updatedFacility.getName());
        facility.setIcon(icon.isEmpty() ? null : uploadIcon(icon));
        return facilityRepository.save(facility);
    }

    @Override
    public void deleteFacility(int id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Facility not found")
        );
        facilityRepository.delete(facility);
    }
}
