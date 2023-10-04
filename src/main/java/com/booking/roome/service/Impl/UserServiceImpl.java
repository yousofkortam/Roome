package com.booking.roome.service.Impl;

import com.booking.roome.dto.reservationDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.exception.ExceptionRequest;
import com.booking.roome.mapper.UserMapper;
import com.booking.roome.model.*;
import com.booking.roome.repository.*;
import com.booking.roome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final HotelRepository hotelRepo;
    private final ReservationRepository reservationRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepo,
                           RoleRepository roleRepo,
                           HotelRepository hotelRepo,
                           ReservationRepository reservationRepository,
                           UserMapper userMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.hotelRepo = hotelRepo;
        this.reservationRepository = reservationRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> users() {
        return userRepo.findAll();
    }

    @Override
    public ResponseEntity<?> getUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ExceptionResponse("User not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<?> addUser(userDto newUser) {
        SaveUserOrElseThrow(newUser);

        return ResponseEntity.ok(new ExceptionRequest("User added successfully", HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<?> updateUser(userDto updatedUser) {
        SaveUserOrElseThrow(updatedUser);

        return ResponseEntity.ok(new ExceptionRequest("User updated successfully", HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<?> deleteUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ExceptionResponse("User not found", HttpStatus.NOT_FOUND));

        try {
            userRepo.delete(user);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new ExceptionRequest("User deleted successfully", HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<?> addHotelToFavorites(int userId, int hotelId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ExceptionResponse("User not found", HttpStatus.NOT_FOUND));

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new ExceptionResponse("Hotel not found", HttpStatus.NOT_FOUND));

        boolean isInUserFav = user.isInFavorite(hotel);
        if (isInUserFav) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ExceptionRequest("Hotel already found", HttpStatus.BAD_REQUEST.value())
            );
        }

        user.addFavorite(hotel);

        try{
            userRepo.save(user);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(
                new ExceptionRequest(hotel.getName() + " added to favorites successfully", HttpStatus.OK.value())
        );
    }

    @Override
    public ResponseEntity<?> removeHotelFromFavorites(int userId, int hotelId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ExceptionResponse("User cannot be null", HttpStatus.BAD_REQUEST));

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new ExceptionResponse("Hotel cannot be null", HttpStatus.BAD_REQUEST));

        boolean isFavExist = user.isInFavorite(hotel);
        user.removeFavorite(hotel);

        if (isFavExist) {
            try {
                userRepo.save(user);
            }catch (Exception e) {
                throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(new ExceptionRequest(hotel.getName() + " removed from favorites successfully", HttpStatus.OK.value()));
        }

        // return failed response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionRequest("This hotel is not in your favorites", HttpStatus.NOT_FOUND.value()));
    }

    @Override
    public ResponseEntity<?> bookHotel(reservationDto reservation) {
        User user = userRepo.findById(reservation.getUser_id()).orElseThrow(() -> new ExceptionResponse("User cannot be null", HttpStatus.BAD_REQUEST));

        Hotel hotel = hotelRepo.findById(reservation.getHotel_id()).orElseThrow(() -> new ExceptionResponse("Hotel cannot be null", HttpStatus.BAD_REQUEST));

        if (!canBookThisHotel(hotel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionRequest("No more rooms", HttpStatus.NOT_FOUND.value()));
        }
        Reservation res = new Reservation();
        res.setUser(user); res.setHotel(hotel); res.setCheckInDate(reservation.getCheckInDate());
        res.setCheckOutDate(res.getCheckOutDate()); res.setRoomNumber(reservation.getRoom_number());

        try {
            reservationRepository.save(res);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new ExceptionRequest("Booked successfully", HttpStatus.OK.value()));
    }

    private boolean canBookThisHotel(Hotel hotel) {
        int totalNumberOfRooms = hotel.getNumberRooms();
        int numberOfReservation = hotel.getReservations().size();
        return numberOfReservation < totalNumberOfRooms;
    }

    int isUserExist(String username, String email) {
        if (userRepo.existsByUsername(username)) return 1;
        if (userRepo.existsByEmail(email)) return 2;
        return 3;
    }

    private void SaveUserOrElseThrow(userDto newUser) {
        int isExist = isUserExist(newUser.getUsername(), newUser.getEmail());
        if (isExist == 1) throw new ExceptionResponse("Username already in use", HttpStatus.BAD_REQUEST);
        if (isExist == 2) throw new ExceptionResponse("Email already in use", HttpStatus.BAD_REQUEST);

        Role role;
        if (newUser.getRole_id() == 0) {
            role = roleRepo.findByName("user");
        }else {
            role = roleRepo.findById(newUser.getRole_id()).orElseThrow(() -> new ExceptionResponse("Role not found", HttpStatus.NOT_FOUND));
        }

        User user = userMapper.toEntity(newUser);
        user.setRole(role);

        try {
            userRepo.save(user);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
