package com.booking.roome.service.hotel;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.mapper.HotelMapper;
import com.booking.roome.model.Facility;
import com.booking.roome.model.Hotel;
import com.booking.roome.model.Image;
import com.booking.roome.model.User;
import com.booking.roome.repository.*;
import com.booking.roome.service.facility.FacilityService;
import com.booking.roome.service.uploadFile.UploadFileService;
import com.booking.roome.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final UserService userService;
    private final FacilityService facilityService;
    private final HotelMapper hotelMapper;
    private final ImageRepository imageRepository;
    private final UploadFileService uploadFileService;

    @Override
    public List<Hotel> hotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotel(int id) {
        return hotelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Hotel not found")
        );
    }

    @Override
    @Transactional
    public Hotel addHotel(hotelDto hotel, MultipartFile[] Files) {
        User admin = userService.getUser(hotel.getAdmin_id());
        Hotel newHotel = hotelMapper.toEntity(hotel);
        newHotel.setAdmin(admin);
        List<Facility> facilities = facilityService.findAllById(hotel.getFacilities());
        for (Facility facility : facilities) {
            newHotel.addFacility(facility);
        }
        UploadImages(Files, newHotel);
        return hotelRepository.save(newHotel);
    }

    @Override
    @Transactional
    public Hotel updateHotel(hotelDto hotel, int id, MultipartFile[] Files) {
        Hotel oldHotel = getHotel(id);

        User admin = userService.getUser(hotel.getAdmin_id());

        oldHotel.setName(hotel.getName());
        oldHotel.setAdmin(admin);
        oldHotel.setDescription(hotel.getDescription());
        oldHotel.setLocation(hotel.getLocation());
        oldHotel.setRate(hotel.getRate());
        oldHotel.setPrice(hotel.getPrice());
        oldHotel.setNumberRooms(hotel.getNumberRooms());

        UploadImages(Files, oldHotel);

        List<Facility> facilities = facilityService.findAllById(hotel.getFacilities());

        oldHotel.setFacilities(facilities);

        return hotelRepository.save(oldHotel);
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
            imageRepository.saveAll(images);
            hotel.setImages(images);
        }
    }

    @Override
    public void deleteHotel(int id) {
        Hotel hotel = getHotel(id);
        hotelRepository.delete(hotel);
    }

    @Override
    public List<Hotel> search(String name) {
        return hotelRepository.findByNameContaining(name);
    }
}
