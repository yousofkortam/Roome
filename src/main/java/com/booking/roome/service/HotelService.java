package com.booking.roome.service;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    List<Hotel> hotels();
    ResponseEntity<?> getHotel(int id);
    ResponseEntity<?> addHotel(hotelDto hotel, MultipartFile[] File);
    ResponseEntity<?> updateHotel(hotelDto hotel, int id, MultipartFile[] files);
    ResponseEntity<?> deleteHotel(int id);
    List<Hotel> search(String name);
}
