package com.booking.roome.service;

import com.booking.roome.dto.loginDto;
import com.booking.roome.dto.userDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(loginDto login);
    ResponseEntity<?> register(userDto user);
}
