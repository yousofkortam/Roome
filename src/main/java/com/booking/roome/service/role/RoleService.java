package com.booking.roome.service.role;

import com.booking.roome.dto.roleDto;
import com.booking.roome.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    List<Role> roles();
    Role getRole(Integer id);
    Role add(roleDto newRole);
    Role update(Role role, int id);
    void delete(int id);
}
