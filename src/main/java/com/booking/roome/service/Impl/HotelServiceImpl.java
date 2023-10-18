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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final HotelMapper hotelMapper;
    private final ImageRepository imageRepository;



    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository,
                            UserRepository userRepository,
                            FacilityRepository facilityRepository,
                            HotelMapper hotelMapper,
                            ImageRepository imageRepository) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.hotelMapper = hotelMapper;
        this.imageRepository = imageRepository;
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
    public ResponseEntity<?> addHotel(hotelDto hotel, MultipartFile[] File) {
        User admin = userRepository.findById(hotel.getAdmin_id()).orElseThrow(() -> new ExceptionResponse("Admin not found", HttpStatus.NOT_FOUND));

        Hotel newHotel = hotelMapper.toEntity(hotel);
        newHotel.setAdmin(admin);
        List<Facility> facilities = facilityRepository.findAllById(hotel.getFacilities());

        for (Facility facility : facilities) {
            newHotel.addFacility(facility);
        }

        List<Image> images = uploadImage(File);

        if (!images.isEmpty()) {
            newHotel.addAllImages(images);
        }

        try {
            hotelRepository.save(newHotel);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(newHotel);
    }

    private List<Image> uploadImage(MultipartFile[] File) {
        String UPLOADED_FOLDER = "src/main/resources/static/images/";
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : File) {
            try {

                String filename = getUniqueFileName(file);

                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + filename);
                Files.write(path, bytes);

                Image image = new Image();
                image.setPath(filename); image.setName(file.getOriginalFilename());
                images.add(image);

            } catch (Exception e) {
                throw new ExceptionResponse("Failed to upload file", HttpStatus.BAD_REQUEST);
            }
        }
        imageRepository.saveAll(images);
        return images;
    }

    private String getUniqueFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : null;
        return (originalFilename != null ? originalFilename.substring(0, originalFilename.lastIndexOf('.')) : null) + new Date().getTime() + extension;
    }

    @Override
    public ResponseEntity<?> updateHotel(hotelDto hotel, int id, MultipartFile[] files) {
        Hotel oldHotel = hotelRepository.findById(id).orElseThrow(() -> new ExceptionResponse("Hotel not found", HttpStatus.NOT_FOUND));

        User admin = userRepository.findById(hotel.getAdmin_id()).orElseThrow(() -> new ExceptionResponse("Admin not found", HttpStatus.NOT_FOUND));

        oldHotel.setName(hotel.getName());
        oldHotel.setAdmin(admin);
        oldHotel.setDescription(hotel.getDescription());
        oldHotel.setLocation(hotel.getLocation());
        oldHotel.setRate(hotel.getRate());
        oldHotel.setPrice(hotel.getPrice());
        oldHotel.setNumberRooms(hotel.getNumberRooms());

        List<Image> images = uploadImage(files);
        oldHotel.setImages(images);

        List<Facility> facilities = facilityRepository.findAllById(hotel.getFacilities());

        oldHotel.setFacilities(facilities);

        try {
            hotelRepository.save(oldHotel);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(oldHotel);
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
