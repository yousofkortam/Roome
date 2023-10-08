package com.booking.roome.service.Impl;

import com.booking.roome.dto.loginDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.exception.ExceptionRequest;
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
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

        user = userRepo.getUserByEmailAndPassword(username, password);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionRequest("Username or password incorrect", HttpStatus.NOT_FOUND.value())
        );
    }

    @Override
    public ResponseEntity<?> register(userDto newUser) {
        if (userRepo.existsByUsername(newUser.getUsername())) throw new ExceptionResponse("Username already in user", HttpStatus.BAD_REQUEST);
        if (userRepo.existsByEmail(newUser.getEmail())) throw new ExceptionResponse("Email already in user", HttpStatus.BAD_REQUEST);

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
            throw new ExceptionResponse("Validation failed", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(user);
    }
}
