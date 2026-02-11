package app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.util.HashMap;

import app.dto.UserBasic;
import app.security.RequiresPermission;
import app.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users (id and name only)
     * Requires user.read permission
     */
    @GetMapping("/users")
    @RequiresPermission("user.read")
    public List<UserBasic> getUsers(@RequestParam(value = "role", required = false) Long roleId) {
        if (roleId != null) {
            return userService.getAllUsersByRole(roleId);
        }
        return userService.getAllBasic();
    }

    /**
     * Update current user's name and/or email
     * Only allows the authenticated user to update their own info
     * Requires user.update.self permission
     */
    @PatchMapping("/users/me")
    public Map<String, String> updateMe(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> updates) {
        String username = userDetails.getUsername();
        String name = updates.get("name");
        String email = updates.get("email");
        var updated = userService.updateCurrentUser(username, name, email);
        Map<String, String> result = new HashMap<>();
        result.put("name", updated.name());
        // If email was updated, return the new email, otherwise return the current one
        result.put("email", email != null && !email.isBlank() ? email : userDetails.getUsername());
        return result;
    }

    /**
     * Change current user's password
     * Only allows the authenticated user to change their own password
     * Body: { "currentPassword": "...", "newPassword": "..." }
     */
    @PatchMapping("/users/me/password")
    public Map<String, String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body) {
        String username = userDetails.getUsername();
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");
        userService.changeCurrentUserPassword(username, currentPassword, newPassword);
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Password updated");
        return result;
    }
}