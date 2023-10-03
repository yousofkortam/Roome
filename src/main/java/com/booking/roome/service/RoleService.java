package com.booking.roome.service;

import com.booking.roome.dto.roleDto;
import com.booking.roome.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    List<Role> roles();
    ResponseEntity<?> getRole(Integer id);
    ResponseEntity<?> add(roleDto newRole);
    ResponseEntity<?> update(Role role, int id);
    ResponseEntity<?> delete(Integer id);
}
