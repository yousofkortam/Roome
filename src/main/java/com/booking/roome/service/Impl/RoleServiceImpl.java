package com.booking.roome.service.Impl;

import com.booking.roome.dto.roleDto;
import com.booking.roome.exception.ExceptionResponse;
import com.booking.roome.exception.ExceptionRequest;
import com.booking.roome.model.Role;
import com.booking.roome.repository.RoleRepository;
import com.booking.roome.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    private boolean isRoleFound(String name) {
        return roleRepo.findByName(name.toLowerCase()) != null;
    }
    @Override
    public ResponseEntity<?> getRole(Integer id) {
        Role role = roleRepo.findById(id).orElseThrow(() -> new ExceptionResponse("Role not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(role);
    }

    @Override
    public ResponseEntity<?> add(roleDto newRole) {
        boolean isExist = isRoleFound(newRole.getName().toLowerCase());
        if (!isExist) {
            Role role = new Role();
            role.setName(newRole.getName().toLowerCase());

            try {
                roleRepo.save(role);
            }catch (Exception e) {
                throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok(role);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionRequest("Role already exist", HttpStatus.BAD_REQUEST.value()));
    }

    @Override
    public List<Role> roles() {
        return roleRepo.findAll();
    }

    @Override
    public ResponseEntity<?> update(Role role, int id) {
        Role oldRole = roleRepo.findById(id).orElseThrow(() -> new ExceptionResponse("Role not found", HttpStatus.NOT_FOUND));

        oldRole.setName(role.getName().toLowerCase());

        try {
            roleRepo.save(oldRole);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(role);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Role role = roleRepo.findById(id).orElseThrow(() -> new ExceptionResponse("Role not found", HttpStatus.NOT_FOUND));

        try {
            roleRepo.delete(role);
        }catch (Exception e) {
            throw new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new ExceptionRequest("Role deleted successfully", HttpStatus.OK.value()));
    }
}
