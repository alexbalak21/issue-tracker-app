package app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import app.model.Permission;
import app.model.Role;
import app.security.RequiresPermission;
import app.service.PermissionService;
import app.service.RoleService;
import app.dto.CreatePermissionRequest;
import app.dto.CreateRoleRequest;

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
    public Permission createPermission(@RequestBody CreatePermissionRequest req) {
        return permissionService.create(req.name(), req.description());
    }

    @GetMapping("/permissions")
    @RequiresPermission("admin.manage")
    public List<Permission> getPermissions() {
        return permissionService.getAll();
    }

    // -----------------------------
    // Roles
    // -----------------------------

    @PostMapping("/roles")
    @RequiresPermission("admin.manage")
    public Role createRole(@RequestBody CreateRoleRequest req) {
        return roleService.create(req.name(), req.description(), req.permissionIds());
    }

    @GetMapping("/roles")
    @RequiresPermission("admin.manage")
    public List<Role> getRoles() {
        return roleService.getAll();
    }
}
