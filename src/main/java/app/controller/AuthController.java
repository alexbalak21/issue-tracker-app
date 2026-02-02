package app.controller;

import app.dto.ApiResponse;
import app.dto.LoginRequest;
import app.dto.RefreshTokenRequest;
import app.dto.RegisterRequest;
import app.dto.UserInfo;
import app.security.CustomUserDetails;
import app.service.AuthService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ----------------------------------------------------
    // Login
    // ----------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest req) {
        log.info("Login attempt for email: {}", req.getEmail());

        try {
            Map<String, String> tokens = authService.login(req);

            // Extract authenticated user from SecurityContext
            var auth = SecurityContextHolder.getContext().getAuthentication();
            var userDetails = (CustomUserDetails) auth.getPrincipal();
            var userInfo = new UserInfo(userDetails);

            Map<String, Object> data = new HashMap<>();
            data.put("user", userInfo);
            data.put("access_token", tokens.get("access_token"));
            data.put("refresh_token", tokens.get("refresh_token"));

            return ResponseEntity.ok(new ApiResponse<>("Login successful", data));

        } catch (RuntimeException e) {
            log.error("Login failed for {}: {}", req.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }

    // ----------------------------------------------------
    // Register
    // ----------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest req) {
        log.info("Registration attempt for {}", req.getEmail());

        try {
            var user = authService.register(req);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            "User registered successfully",
                            user.getName()));

        } catch (RuntimeException e) {
            log.error("Registration failed for {}: {}", req.getEmail(), e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails user) {
            return ResponseEntity.ok(new UserInfo(user));
        }

        return ResponseEntity.status(500).build();
    }

    // ----------------------------------------------------
    // Refresh Token
    // ----------------------------------------------------
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        log.info("Refresh token attempt");

        try {
            var tokens = authService.refreshToken(req.getRefreshToken());
            return ResponseEntity.ok(new ApiResponse<>("Token refreshed successfully", tokens));

        } catch (RuntimeException e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }
}
