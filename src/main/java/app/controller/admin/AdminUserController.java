package app.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.dto.UserDto;
import app.dto.CreateUserRequest;
import app.dto.AssignRolesRequest;
import app.security.RequiresPermission;
import app.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // ----------------------------------------------------
    // Create user
    // ----------------------------------------------------
    @PostMapping
    @RequiresPermission("admin.manage")
    public UserDto createUser(@RequestBody CreateUserRequest req) {
        return userService.create(
                req.name(),
                req.email(),
                req.password(),
                req.roleIds());
    }

    // GET ONE USER
    @GetMapping("/{userId}")
    @RequiresPermission("admin.manage")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    // ----------------------------------------------------
    // List all users
    // ----------------------------------------------------
    @GetMapping
    @RequiresPermission("admin.manage")
    public List<UserDto> getUsers() {
        return userService.getAll();
    }

    // ----------------------------------------------------
    // Assign roles to a user
    // ----------------------------------------------------
    @PostMapping("/{userId}/roles")
    @RequiresPermission("admin.manage")
    public UserDto assignRoles(
            @PathVariable Long userId,
            @RequestBody AssignRolesRequest req) {
        return userService.assignRoles(userId, req.roleIds());
    }

    // ----------------------------------------------------
    // Remove roles from a user
    // ----------------------------------------------------
    @DeleteMapping("/{userId}/roles")
    @RequiresPermission("admin.manage")
    public UserDto removeRoles(
            @PathVariable Long userId,
            @RequestBody AssignRolesRequest req) {
        return userService.removeRoles(userId, req.roleIds());
    }
}
