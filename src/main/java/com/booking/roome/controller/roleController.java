package com.booking.roome.controller;

import com.booking.roome.dto.roleDto;
import com.booking.roome.model.Role;
import com.booking.roome.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class roleController {
    private final RoleService roleService;

    @Autowired
    public roleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable int id) {
        return roleService.getRole(id);
    }

    @GetMapping()
    public List<Role> roles() {
        return roleService.roles();
    }

    @PostMapping()
    public ResponseEntity<?> addRole(@Valid @RequestBody roleDto newRole) {
        return roleService.add(newRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@Valid @RequestBody Role role, @PathVariable int id) {
        return ResponseEntity.ok(roleService.update(role, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable int id) {
        return roleService.delete(id);
    }

}
