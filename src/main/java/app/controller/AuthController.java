package app.controller;

import app.dto.ApiResponse;
import app.dto.LoginRequest;
import app.dto.RefreshTokenRequest;
import app.dto.RegisterRequest;
import app.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login attempt for email: {}", loginRequest.getEmail());
            var loginResult = userService.login(loginRequest);

            // After login(), SecurityContext is now populated
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            var userDetails = (app.security.CustomUserDetails) auth.getPrincipal();
            var userDto = new app.dto.UserInfo(userDetails);

            java.util.Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("user", userDto);
            responseData.put("access_token", loginResult.get("access_token"));
            responseData.put("refresh_token", loginResult.get("refresh_token"));
            return ResponseEntity
                    .ok(new ApiResponse<>("Login successful", responseData));
        } catch (RuntimeException e) {
            log.error("Login failed for email {}: {}", loginRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            log.info("Received registration request for user: {}", registerRequest.getName());
            var user = userService.registerUser(registerRequest);
            log.info("User registered successfully: {}", user.getName());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            "User registered successfully",
                            user.getName()
                    ));
        } catch (RuntimeException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        try {
            log.info("Token refresh attempt");
            var tokens = userService.refreshToken(refreshRequest.getRefreshToken());
            return ResponseEntity
                    .ok(new ApiResponse<>("Token refreshed successfully", tokens));
        } catch (RuntimeException e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }
}
