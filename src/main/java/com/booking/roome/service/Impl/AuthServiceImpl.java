package com.booking.roome.service.Impl;

import com.booking.roome.dto.loginDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.mapper.UserMapper;
import com.booking.roome.model.Role;
import com.booking.roome.model.User;
import com.booking.roome.repository.RoleRepository;
import com.booking.roome.repository.UserRepository;
import com.booking.roome.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepo,
                           UserMapper userMapper,
                           RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.roleRepo = roleRepo;
    }

    @Override
    public ResponseEntity<?> login(loginDto login) {
        String username = login.getUsername(), password = login.getPassword();
        User user = userRepo.getUserByUsernameAndPassword(username, password);
        if (user == null) {
            user = userRepo.getUserByEmailAndPassword(username, password);
        }

        if (user == null) {
            throw new ExceptionResponse("Invalid username or password", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<?> register(userDto newUser) {
        if (userRepo.existsByUsername(newUser.getUsername())) throw new ExceptionResponse("Username already in user", HttpStatus.BAD_REQUEST);
        if (userRepo.existsByEmail(newUser.getEmail())) throw new ExceptionResponse("Email already in user", HttpStatus.BAD_REQUEST);

        if (!isValidEmailFormat(newUser.getEmail())) {
            throw new ExceptionResponse("Please enter a valid email", HttpStatus.NOT_FOUND);
        }

        Role role = roleRepo.findById(newUser.getRole_id()).orElse(roleRepo.findByName("user"));

        User user = userMapper.toEntity(newUser);
        user.setRole(role);

        try {
            userRepo.save(user);
        }catch (Exception e) {
            throw new ExceptionResponse("Validation failed", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(user);
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
