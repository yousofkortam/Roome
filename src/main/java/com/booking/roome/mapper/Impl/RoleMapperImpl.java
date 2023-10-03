package com.booking.roome.mapper.Impl;

import com.booking.roome.dto.roleDto;
import com.booking.roome.mapper.RoleMapper;
import com.booking.roome.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperImpl implements RoleMapper {
    @Override
    public Role toEntity(roleDto dto) {
        return Role.builder()
                .name(dto.getName())
                .build();
    }
}
