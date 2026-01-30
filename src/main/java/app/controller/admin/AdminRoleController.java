package app.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import app.dto.CreateRoleRequest;
import app.dto.RoleDto;
import app.security.RequiresPermission;
import app.service.RoleService;

@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {

    private final RoleService roleService;

    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @RequiresPermission("admin.manage")
    public RoleDto createRole(@RequestBody CreateRoleRequest req) {
        return RoleDto.from(
                roleService.create(req.name(), req.description(), req.permissionIds())
        );
    }

    @GetMapping
    @RequiresPermission("admin.manage")
    public List<RoleDto> getRoles() {
        return roleService.getAll().stream()
                .map(RoleDto::from)
                .toList();
    }

    // UPDATE ROLE
    @PutMapping("/{roleId}")
    @RequiresPermission("admin.manage")
    public RoleDto updateRole(@PathVariable Long roleId, @RequestBody CreateRoleRequest req) {
        return RoleDto.from(
                roleService.update(roleId, req.name(), req.description(), req.permissionIds())
        );
    }

    // PATCH ADD PERMISSIONS TO ROLE
    @PatchMapping("/{roleId}/permissions")
    @RequiresPermission("admin.manage")
    public RoleDto addPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        return RoleDto.from(
                roleService.addPermissions(roleId, permissionIds)
        );
    }
}