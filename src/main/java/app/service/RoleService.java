package app.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.model.Permission;
import app.model.Role;
import app.repository.PermissionRepository;
import app.repository.RoleRepository;

@Service
public class RoleService {

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
}