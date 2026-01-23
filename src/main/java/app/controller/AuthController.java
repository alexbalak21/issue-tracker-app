package app.controller;

import app.dto.ApiResponse;
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

    @GetMapping("/login")
    public String login() {
        return "login";
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
                            true,
                            "User registered successfully",
                            user.getName()
                    ));
        } catch (RuntimeException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}
