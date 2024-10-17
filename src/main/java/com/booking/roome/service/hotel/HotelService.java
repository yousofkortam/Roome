package com.booking.roome.service.hotel;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    List<Hotel> hotels();
    Hotel getHotel(int id);
    Hotel addHotel(hotelDto hotel, MultipartFile[] File);
    Hotel updateHotel(hotelDto hotel, int id, MultipartFile[] files);
    void deleteHotel(int id);
    List<Hotel> search(String name);
}
