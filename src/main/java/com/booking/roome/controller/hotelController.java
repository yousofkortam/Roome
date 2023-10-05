package com.booking.roome.controller;

import com.booking.roome.dto.hotelDto;
import com.booking.roome.model.Hotel;
import com.booking.roome.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> addHotel(@Valid @RequestPart("hotel") hotelDto hotel, @RequestPart("files") MultipartFile[] files) {
        return hotelService.addHotel(hotel, files);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@Valid @RequestPart("hotel") hotelDto hotel, @PathVariable int id, @RequestPart("files") MultipartFile[] files) {
        return hotelService.updateHotel(hotel, id, files);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable int id) {
        return hotelService.deleteHotel(id);
    }

    @GetMapping("/search/{name}")
    public List<Hotel> searchHotel(@PathVariable String name) {
        return hotelService.search(name);
    }
}
