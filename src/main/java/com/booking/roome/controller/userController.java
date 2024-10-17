package com.booking.roome.controller;

import com.booking.roome.dto.reservationDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.model.Hotel;
import com.booking.roome.model.User;
import com.booking.roome.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class userController {
    private final UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> users() {
        return userService.users();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody userDto user) {
        return userService.addUser(user);
    }

    @PutMapping("{id}")
    public User updateUser(@Valid @RequestBody userDto user, @PathVariable int id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @GetMapping("/fav/{id}")
    public List<Hotel> getFavoritesByUserId(@PathVariable int id) {
        return userService.getFavByUserId(id);
    }

    @GetMapping("/near-hotels/{id}")
    public List<Hotel> getNearHotels(@PathVariable int id) {
        return userService.getNearMeHotel(id);
    }

    @GetMapping("/recommended-hotels/{id}")
    public List<Hotel> getRecommendedHotels(@PathVariable int id) {
        return userService.getRecommendedHotels(id);
    }

    @PostMapping("/add-to-fav/{userId}/hotel/{hotelId}")
    public ResponseEntity<?> addHotelToFavorites(@PathVariable int userId, @PathVariable int hotelId) {
        return userService.addHotelToFavorites(userId, hotelId);
    }

    @PostMapping("/remove-from-fav/{userId}/hotel/{hotelId}")
    public ResponseEntity<?> removeHotelFromFavorites(@PathVariable int userId, @PathVariable int hotelId) {
        return userService.removeHotelFromFavorites(userId, hotelId);
    }

    @PostMapping("/book-hotel")
    public ResponseEntity<?> bookHotel(@RequestBody reservationDto reservation) {
        return userService.bookHotel(reservation);
    }
}
