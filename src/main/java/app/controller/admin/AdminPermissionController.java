package app.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import app.dto.CreatePermissionRequest;
import app.dto.PermissionDto;
import app.security.RequiresPermission;
import app.service.PermissionService;

@RestController
@RequestMapping("/api/admin/permissions")
public class AdminPermissionController {

    private final PermissionService permissionService;

    public AdminPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    @RequiresPermission("admin.manage")
    public PermissionDto createPermission(@RequestBody CreatePermissionRequest req) {
        return PermissionDto.from(
                permissionService.create(req.name(), req.description())
        );
    }

    @GetMapping
    @RequiresPermission("admin.manage")
    public List<PermissionDto> getPermissions() {
        return permissionService.getAll().stream()
                .map(PermissionDto::from)
                .toList();
    }
}
