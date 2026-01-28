package app.service;

import app.dto.LoginRequest;
import app.dto.RegisterRequest;
import app.model.User;
import app.model.UserRole;
import app.repository.UserRepository;
import app.security.JwtService;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User(
            registerRequest.getName(),
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getEmail(),
            UserRole.USER  // Default role
        );

        return userRepository.save(user);
    }

    public Map<String, String> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Set authentication in SecurityContext
        app.security.CustomUserDetails userDetails = new app.security.CustomUserDetails(user);
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken =
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authToken);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        java.util.Map<String, String> result = new java.util.HashMap<>();
        result.put("access_token", accessToken);
        result.put("refresh_token", refreshToken);
        return result;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        // Extract user ID from refresh token
        Long userId = jwtService.getUserIdFromRefreshToken(refreshToken);

        // Fetch user from database
        if (userId == null) {
            throw new RuntimeException("User ID extracted from refresh token is null");
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        return Map.of(
                "access_token", newAccessToken,
                "refresh_token", newRefreshToken
        );
    }
}
