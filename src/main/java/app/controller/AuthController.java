package app.controller;

import app.dto.ApiResponse;
import app.dto.RegisterRequest;
import app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            log.info("Received registration request for user: {}", registerRequest.getUsername());
            var user = userService.registerUser(registerRequest);
            log.info("User registered successfully: {}", user.getUsername());
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "User registered successfully",
                            user.getUsername()
                    ));
        } catch (RuntimeException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}
