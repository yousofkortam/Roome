package com.booking.roome.service;

import com.booking.roome.dto.reservationDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.model.Hotel;
import com.booking.roome.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> users();
    ResponseEntity<?> getUser(int id);
    ResponseEntity<?> addUser(userDto newUser);
    ResponseEntity<?> updateUser(userDto updatedUser, int id);
    ResponseEntity<?> deleteUser(int id);
    ResponseEntity<?> addHotelToFavorites(int userId, int hotelId);
    ResponseEntity<?> removeHotelFromFavorites(int userId, int hotelId);
    ResponseEntity<?> bookHotel(reservationDto reservation);
    List<Hotel> getFavByUserId(int id);
    List<Hotel> getNearMeHotel(int id);
    List<Hotel> getRecommendedHotels(int id);
}
