package com.booking.roome.service.auth;

import com.booking.roome.dto.loginDto;
import com.booking.roome.dto.userDto;
import com.booking.roome.mapper.UserMapper;
import com.booking.roome.model.Role;
import com.booking.roome.model.User;
import com.booking.roome.repository.RoleRepository;
import com.booking.roome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserMapper userMapper;

    @Override
    public User login(loginDto login) {
        String username = login.getUsername(), password = login.getPassword();
        User user = userRepo.getUserByUsernameAndPassword(username, password);
        if (user == null) {
            user = userRepo.getUserByEmailAndPassword(username, password);
        }

        if (user == null || !user.isActive()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    @Override
    public User register(userDto newUser) {
        if (userRepo.existsByUsername(newUser.getUsername())) throw new IllegalArgumentException("Username already in user");
        if (userRepo.existsByEmail(newUser.getEmail())) throw new IllegalArgumentException("Email already in user");

        if (!isValidEmailFormat(newUser.getEmail())) {
            throw new IllegalArgumentException("Please enter a valid email");
        }

        Role role = roleRepo.findById(newUser.getRole_id()).orElse(roleRepo.findByName("user"));

        User user = userMapper.toEntity(newUser);
        user.setRole(role);
        user.setActive(true);

        return userRepo.save(user);
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
