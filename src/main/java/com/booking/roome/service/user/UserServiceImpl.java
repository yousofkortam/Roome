package com.booking.roome.service.user;

import com.booking.roome.dto.reservationDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.mapper.UserMapper;
import com.booking.roome.model.*;
import com.booking.roome.repository.*;
import com.booking.roome.service.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleService roleService;
    private final HotelRepository hotelRepo;
    private final ReservationRepository reservationRepository;
    private final UserMapper userMapper;

    @Override
    public List<User> users() {
        return userRepo.getAllByActive(true);
    }

    @Override
    public User getUser(int id) {
        return userRepo.getUserByIdAndActive(id, true).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public User addUser(userDto newUser) {
        if (isEmailInUse(newUser.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (isUsernameInUse(newUser.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }

        Role role = roleService.getRole(newUser.getRole_id());

        User user = userMapper.toEntity(newUser);
        user.setRole(role);
        user.setActive(true);

        return userRepo.save(user);
    }

    @Override
    public User updateUser(userDto updatedUser, int id) {
        User existUser = userRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        if (!updatedUser.getEmail().equals(existUser.getEmail()) && isEmailInUse(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (!updatedUser.getUsername().equals(existUser.getUsername()) && isUsernameInUse(updatedUser.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        Role role = roleService.getRole(updatedUser.getRole_id());

        User user = userMapper.toEntity(updatedUser);
        user.setId(id);
        user.setRole(role);
        user.setActive(true);
        user.setManagedHotels(existUser.getManagedHotels());
        user.setFavorites(existUser.getFavorites());
        user.setReservations(existUser.getReservations());

        return userRepo.save(user);
    }

    @Override
    public void deleteUser(int id) {
        User user = getUser(id);
        user.setActive(false);
        userRepo.save(user);
    }

    @Override
    public ResponseEntity<?> addHotelToFavorites(int userId, int hotelId) {
        User user = getUser(userId);

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(
                () -> new EntityNotFoundException("Hotel not found")
        );

        boolean isInUserFav = user.isInFavorite(hotel);
        if (isInUserFav) {
            throw new IllegalArgumentException("This hotel is already in your favorites");
        }

        user.addFavorite(hotel);

        userRepo.save(user);

        return ResponseEntity.ok(hotel.getName() + " added to favorites successfully");
    }

    @Override
    public ResponseEntity<?> removeHotelFromFavorites(int userId, int hotelId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User cannot be null"));

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(
                () -> new EntityNotFoundException("Hotel not found")
        );

        boolean isFavExist = user.isInFavorite(hotel);
        user.removeFavorite(hotel);

        if (isFavExist) {
            userRepo.save(user);
            return ResponseEntity.ok(hotel.getName() + " removed from favorites successfully");
        }

        // return failed response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This hotel is not in your favorites");
    }

    @Override
    public ResponseEntity<?> bookHotel(reservationDto reservation) {
        User user = userRepo.findById(reservation.getUser_id()).orElseThrow(() -> new IllegalArgumentException("User cannot be null"));

        Hotel hotel = hotelRepo.findById(reservation.getHotel_id()).orElseThrow(
                () -> new EntityNotFoundException("Hotel not found")
        );

        if (!canBookThisHotel(hotel)) {
            throw new IllegalArgumentException("This hotel is fully booked");
        }
        Reservation res = new Reservation();
        res.setUser(user); res.setHotel(hotel); res.setCheckInDate(reservation.getCheckInDate());
        res.setCheckOutDate(res.getCheckOutDate()); res.setRoomNumber(reservation.getRoom_number());

        reservationRepository.save(res);

        return ResponseEntity.ok("Booked successfully");
    }

    @Override
    public List<Hotel> getFavByUserId(int id) {
        User user = getUser(id);
        return user.getFavorites();
    }

    @Override
    public List<Hotel> getNearMeHotel(int id) {
        // Not real algorithm to get near hotels for user, LoL 😂
        List<Hotel> hotels = hotelRepo.findAll(), nearHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (nearHotels.size() > 4) break;
            nearHotels.add(hotel);
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
