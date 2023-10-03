package com.booking.roome.mapper;

import com.booking.roome.dto.userDto;
import com.booking.roome.model.User;

public interface UserMapper {
    User toEntity (userDto dto);
    userDto toDto(User user);
}
