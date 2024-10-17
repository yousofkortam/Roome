package com.booking.roome.controller;

import com.booking.roome.dto.loginDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.model.User;
import com.booking.roome.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class authController {

    private final AuthService authService;

    @PostMapping("login")
    public User login(@RequestBody loginDto data) {
        return authService.login(data);
    }

    @PostMapping("register")
    public User register(@Valid @RequestBody userDto user) {
        return authService.register(user);
    }
}
