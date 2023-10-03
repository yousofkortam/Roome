package com.booking.roome.mapper;

import com.booking.roome.dto.roleDto;
import com.booking.roome.model.Role;

public interface RoleMapper {
    Role toEntity(roleDto dto);
}
