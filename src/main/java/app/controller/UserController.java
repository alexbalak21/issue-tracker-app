package app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<UserBasic> getAllUsers() {
        return userService.getAllBasic();
    }
}