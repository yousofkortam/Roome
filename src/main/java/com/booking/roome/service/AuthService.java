package com.booking.roome.service;

import com.booking.roome.dto.loginDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(loginDto login);
}
