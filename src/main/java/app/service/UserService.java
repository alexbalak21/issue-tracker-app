package app.service;

import app.dto.RegisterRequest;
import app.model.User;
import app.model.UserRole;
import app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
