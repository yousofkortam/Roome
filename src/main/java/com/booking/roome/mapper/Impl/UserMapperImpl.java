package com.booking.roome.mapper.Impl;

import com.booking.roome.dto.userDto;
import com.booking.roome.mapper.UserMapper;
import com.booking.roome.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toEntity(userDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nationality(dto.getNationality())
                .phoneNumber(dto.getPhoneNumber())
                .occupation(dto.getOccupation())
                .profileImage(dto.getProfileImage())
                .build();
    }

    @Override
    public userDto toDto(User user) {
        return userDto.builder()
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role_id(user.getRole().getId())
                .nationality(user.getNationality())
                .phoneNumber(user.getPhoneNumber())
                .occupation(user.getOccupation())
                .profileImage(user.getProfileImage())
                .build();
    }
}
