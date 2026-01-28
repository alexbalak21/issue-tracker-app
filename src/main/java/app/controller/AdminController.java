package app.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import app.security.RequiresPermission;
import app.service.PermissionService;
import app.service.RoleService;
import app.dto.CreatePermissionRequest;
import app.dto.CreateRoleRequest;
import app.dto.PermissionDto;
import app.dto.RoleDto;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    public AdminController(PermissionService permissionService, RoleService roleService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    // -----------------------------
    // Permissions
    // -----------------------------

    @PostMapping("/permissions")
    @RequiresPermission("admin.manage")
    public PermissionDto createPermission(@RequestBody CreatePermissionRequest req) {
        return PermissionDto.from(
                permissionService.create(req.name(), req.description())
        );
    }

    @GetMapping("/permissions")
    @RequiresPermission("admin.manage")
    public List<PermissionDto> getPermissions() {
        return permissionService.getAll().stream()
                .map(PermissionDto::from)
                .toList();
    }

    // -----------------------------
    // Roles
    // -----------------------------

    @PostMapping("/roles")
    @RequiresPermission("admin.manage")
    public RoleDto createRole(@RequestBody CreateRoleRequest req) {
        return RoleDto.from(
                roleService.create(req.name(), req.description(), req.permissionIds())
        );
    }

    @GetMapping("/roles")
    @RequiresPermission("admin.manage")
    public List<RoleDto> getRoles() {
        return roleService.getAll().stream()
                .map(RoleDto::from)
                .toList();
    }
}
