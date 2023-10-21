package com.booking.roome.service.Impl;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.exception.ExceptionRequest;
import com.booking.roome.mapper.HotelMapper;
import com.booking.roome.model.Facility;
import com.booking.roome.model.Hotel;
import com.booking.roome.model.Image;
import com.booking.roome.model.User;
import com.booking.roome.repository.*;
import com.booking.roome.service.HotelService;
import com.booking.roome.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final HotelMapper hotelMapper;
    private final ImageRepository imageRepository;

    private final UploadFileService uploadFileService;



    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository,
                            UserRepository userRepository,
                            FacilityRepository facilityRepository,
                            HotelMapper hotelMapper,
                            ImageRepository imageRepository, UploadFileService uploadFileService) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.hotelMapper = hotelMapper;
        this.imageRepository = imageRepository;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public List<Hotel> hotels() {
        return hotelRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getHotel(int id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Hotel not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }

    @Override
    public ResponseEntity<?> addHotel(hotelDto hotel, MultipartFile[] Files) {
        User admin = userRepository.findById(hotel.getAdmin_id()).orElseThrow(() -> new ExceptionResponse("Admin not found", HttpStatus.NOT_FOUND));

        Hotel newHotel = hotelMapper.toEntity(hotel);
        newHotel.setAdmin(admin);
        List<Facility> facilities = facilityRepository.findAllById(hotel.getFacilities());

        for (Facility facility : facilities) {
            newHotel.addFacility(facility);
        }

        UploadImages(Files, newHotel);

        try {
            hotelRepository.save(newHotel);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(newHotel);
    }

    @Override
    public ResponseEntity<?> updateHotel(hotelDto hotel, int id, MultipartFile[] Files) {
        Hotel oldHotel = hotelRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Hotel not found", HttpStatus.NOT_FOUND));

        User admin = userRepository.findById(hotel.getAdmin_id()).orElseThrow(() -> new ExceptionResponse("Admin not found", HttpStatus.NOT_FOUND));

        oldHotel.setName(hotel.getName());
        oldHotel.setAdmin(admin);
        oldHotel.setDescription(hotel.getDescription());
        oldHotel.setLocation(hotel.getLocation());
        oldHotel.setRate(hotel.getRate());
        oldHotel.setPrice(hotel.getPrice());
        oldHotel.setNumberRooms(hotel.getNumberRooms());

        UploadImages(Files, oldHotel);

        List<Facility> facilities = facilityRepository.findAllById(hotel.getFacilities());

        oldHotel.setFacilities(facilities);

        try {
            hotelRepository.save(oldHotel);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(oldHotel);
    }

    private void UploadImages(MultipartFile[] Files, Hotel hotel) {
        if (Files.length > 0) {
            List<Image> images = new ArrayList<>();
            for (MultipartFile file : Files) {
                String imageUrl = uploadFileService.uploadFile(file);
                images.add(
                        new Image(
                                file.getOriginalFilename(),
                                imageUrl
                        )
                );
            }
            try {
                imageRepository.saveAll(images);
                hotel.setImages(images);
            }catch (Exception e) {
                throw new ExceptionResponse("Cannot upload images", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<?> deleteHotel(int id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Hotel not found", HttpStatus.NOT_FOUND));

        try {
            hotelRepository.delete(hotel);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ExceptionRequest("Hotel deleted successfully", HttpStatus.OK.value())
        );
    }

    @Override
    public List<Hotel> search(String name) {
        return hotelRepository.findByNameContaining(name);
    }
}
