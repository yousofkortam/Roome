package com.booking.roome.service;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.model.Hotel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelService {
    List<Hotel> hotels();
    ResponseEntity<?> getHotel(int id);
    ResponseEntity<?> addHotel(hotelDto hotel);
    ResponseEntity<?> updateHotel(hotelDto hotel, int id);
    ResponseEntity<?> deleteHotel(int id);
}
