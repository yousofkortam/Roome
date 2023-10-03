package com.booking.roome.service.Impl;

import com.booking.roome.dto.loginDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.model.User;
import com.booking.roome.repository.UserRepository;
import com.booking.roome.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    @Autowired
    public AuthServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ResponseEntity<?> login(loginDto login) {
        String username = login.getUsername(), password = login.getPassword();

        User user = userRepo.getUserByUsernameAndPassword(username, password);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

        user = userRepo.getUserByEmailAndPassword(username, password);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse("Username or password incorrect", HttpStatus.NOT_FOUND.value())
        );
    }
}
