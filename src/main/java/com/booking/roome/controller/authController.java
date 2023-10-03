package com.booking.roome.controller;

import com.booking.roome.dto.loginDto;
import com.booking.roome.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authController {
    private final AuthService authService;

    @Autowired
    public authController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginDto data) {
        return authService.login(data);
    }
}
