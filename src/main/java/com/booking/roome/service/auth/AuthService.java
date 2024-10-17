package com.booking.roome.service.auth;

import com.booking.roome.dto.loginDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    User login(loginDto login);
    User register(userDto user);
}
