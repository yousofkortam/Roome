package com.booking.roome.controller;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.model.Hotel;
import com.booking.roome.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hotel")
public class hotelController {
    private final HotelService hotelService;

    @Autowired
    public hotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotel(@PathVariable int id) {
        return hotelService.getHotel(id);
    }

    @GetMapping
    public List<Hotel> getHotels() {
        return hotelService.hotels();
    }

    @PostMapping
    public ResponseEntity<?> addHotel(@RequestBody hotelDto hotel) {
        return hotelService.addHotel(hotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@RequestBody hotelDto hotel, @PathVariable int id) {
        return hotelService.updateHotel(hotel, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable int id) {
        return hotelService.deleteHotel(id);
    }
}
