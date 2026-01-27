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
            // Expecting userService.login to return a map or DTO with user, access_token, refresh_token
            var loginResult = userService.login(loginRequest);
            Object user = null;
            String accessToken = null;
            String refreshToken = null;
            if (loginResult instanceof java.util.Map<?, ?> map) {
                user = map.get("user");
                accessToken = (String) map.get("access_token");
                refreshToken = (String) map.get("refresh_token");
            } else if (loginResult instanceof app.dto.LoginResponse resp) {
                user = resp.getUser();
                accessToken = resp.getAccessToken();
                refreshToken = resp.getRefreshToken();
            }
            var response = new app.dto.LoginResponse(user, accessToken, refreshToken);
            return ResponseEntity
                    .ok(new ApiResponse<>("Login successful", response));
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
