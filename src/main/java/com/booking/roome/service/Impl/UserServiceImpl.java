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
import java.util.ArrayList;
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

    Role getRole(int id) {
        return roleRepo.findById(id).orElse(roleRepo.findByName("user"));
    }

    @Override
    public ResponseEntity<?> addUser(userDto newUser) {
        if (isEmailInUse(newUser.getEmail())) {
            throw new ExceptionResponse("Email already in use", HttpStatus.BAD_REQUEST);
        }
        if (isUsernameInUse(newUser.getUsername())) {
            throw new ExceptionResponse("Username already in use", HttpStatus.BAD_REQUEST);
        }

        Role role = getRole(newUser.getRole_id());

        User user = userMapper.toEntity(newUser);
        user.setRole(role);

        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new ExceptionRequest("User added successfully", HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<?> updateUser(userDto updatedUser, int id) {
        User existUser = userRepo.findById(id).orElseThrow(
                () -> new ExceptionResponse("User not found", HttpStatus.NOT_FOUND)
        );
        if (!updatedUser.getEmail().equals(existUser.getEmail()) && isEmailInUse(updatedUser.getEmail())) {
            throw new ExceptionResponse("Email already in use", HttpStatus.BAD_REQUEST);
        }

        if (!updatedUser.getUsername().equals(existUser.getUsername()) && isUsernameInUse(updatedUser.getUsername())) {
            throw new ExceptionResponse("Username already in use", HttpStatus.BAD_REQUEST);
        }
        Role role = getRole(updatedUser.getRole_id());

        User user = userMapper.toEntity(updatedUser);
        user.setId(id);
        user.setRole(role);
        user.setManagedHotels(existUser.getManagedHotels());
        user.setFavorites(existUser.getFavorites());
        user.setReservations(existUser.getReservations());

        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(user);
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

    @Override
    public List<Hotel> getFavByUserId(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ExceptionResponse("User not found", HttpStatus.NOT_FOUND));
        return user.getFavorites();
    }

    @Override
    public List<Hotel> getNearMeHotel(int id) {
        // Not real algorithm to get near hotels for user, LoL 😂
        List<Hotel> hotels = hotelRepo.findAll(), nearHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (nearHotels.size() >= 3) break;
            boolean done = Math.max((double) (id / 3), hotel.getId()) % Math.min( (double) (id / 3), hotel.getId()) == 0;
            if ( done ) {
                nearHotels.add(hotel);
            }
        }
        return nearHotels;
    }

    @Override
    public List<Hotel> getRecommendedHotels(int id) {
        // Not real algorithm to get recommended hotels for user, LoL 😂
        List<Hotel> hotels = hotelRepo.findAll(), nearHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            boolean done = Math.max(id, hotel.getId()) % Math.min(id, hotel.getId()) == 0;
            if ( done ) {
                nearHotels.add(hotel);
            }
        }
        return nearHotels;
    }

    private boolean canBookThisHotel(Hotel hotel) {
        int totalNumberOfRooms = hotel.getNumberRooms();
        int numberOfReservation = hotel.getReservations().size();
        return numberOfReservation < totalNumberOfRooms;
    }

    private boolean isEmailInUse(String email) {
        return userRepo.existsByEmail(email);
    }

    private boolean isUsernameInUse(String username) {
        return userRepo.existsByUsername(username);
    }

}
