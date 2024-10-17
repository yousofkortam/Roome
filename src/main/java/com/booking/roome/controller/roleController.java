package com.booking.roome.controller;

import com.booking.roome.dto.roleDto;
import com.booking.roome.model.Role;
import com.booking.roome.service.role.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("role")
public class roleController {

    private final RoleService roleService;

    @GetMapping("/{id}")
    public Role getRole(@PathVariable int id) {
        return roleService.getRole(id);
    }

    @GetMapping()
    public List<Role> roles() {
        return roleService.roles();
    }

    @PostMapping()
    public Role addRole(@Valid @RequestBody roleDto newRole) {
        return roleService.add(newRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@Valid @RequestBody Role role, @PathVariable int id) {
        return ResponseEntity.ok(roleService.update(role, id));
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable int id) {
        roleService.delete(id);
    }

}
