package app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.model.Permission;
import app.model.Role;
import app.repository.PermissionRepository;
import app.repository.RoleRepository;

@Service
public class RoleService {
    @Transactional
    public Role update(Long roleId, String name, String description, @NonNull List<Long> permissionIds) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId));
        role.setName(name);
        role.setDescription(description);
        List<Long> safeIds = permissionIds != null ? permissionIds : List.of();
        List<Permission> permissions = permissionRepo.findAllById(safeIds);
        role.setPermissions(new HashSet<>(permissions));
        return roleRepo.save(role);
    }

    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;

    public RoleService(RoleRepository roleRepo, PermissionRepository permissionRepo) {
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    public Role create(
            String name,
            String description,
            @NonNull List<Long> permissionIds) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);

        List<Long> safeIds = permissionIds != null ? permissionIds : List.of();

        List<Permission> permissions = permissionRepo.findAllById(safeIds);
        role.setPermissions(new HashSet<>(permissions));

        return roleRepo.save(role);
    }

    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return roleRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> getById(Long roleId) {
        return roleRepo.findById(roleId);
    }

    @Transactional
    public Role addPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId));
        List<Permission> permissions = permissionRepo.findAllById(permissionIds);
        role.getPermissions().addAll(permissions);
        return roleRepo.save(role);
    }

    @Transactional
    public Role removePermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId));
        List<Permission> permissions = permissionRepo.findAllById(permissionIds);
        role.getPermissions().removeAll(permissions);
        return roleRepo.save(role);
    }
}