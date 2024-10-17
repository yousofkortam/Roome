package com.booking.roome.service.role;

import com.booking.roome.dto.roleDto;
import com.booking.roome.mapper.RoleMapper;
import com.booking.roome.model.Role;
import com.booking.roome.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepo,
                           RoleMapper roleMapper) {
        this.roleRepo = roleRepo;
        this.roleMapper = roleMapper;
    }

    private boolean isRoleFound(String name) {
        return roleRepo.findByName(name.toLowerCase()) != null;
    }
    @Override
    public Role getRole(Integer id) {
        return roleRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role not found")
        );
    }

    @Override
    public Role add(roleDto newRole) {
        boolean isExist = isRoleFound(newRole.getName().toLowerCase());
        if (isExist) {
            throw new IllegalArgumentException("Role already exists");
        }
        Role role = roleMapper.toEntity(newRole);
        return roleRepo.save(role);
    }

    @Override
    public List<Role> roles() {
        return roleRepo.findAll();
    }

    @Override
    public Role update(Role role, int id) {
        Role oldRole = roleRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role not found")
        );
        oldRole.setName(role.getName().toLowerCase());
        return roleRepo.save(oldRole);
    }

    @Override
    public void delete(int id) {
        Role role = roleRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role not found")
        );
        roleRepo.delete(role);
    }
}
