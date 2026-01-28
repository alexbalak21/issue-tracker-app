package app.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.model.Role;
import app.model.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------------------------------
    // Create user (admin)
    // ----------------------------------------------------
    public User create(String name, String email, String password, List<Long> roleIds) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        List<Role> roles = roleRepository.findAllById(
                roleIds != null ? roleIds : List.of()
        );

        User user = new User(
                name,
                passwordEncoder.encode(password),
                email,
                new HashSet<>(roles)
        );

        return userRepository.save(user);
    }

    // ----------------------------------------------------
    // List all users (admin)
    // ----------------------------------------------------
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAllWithRoles(); // requires EntityGraph
    }

    // ----------------------------------------------------
    // Assign roles to user
    // ----------------------------------------------------
    public User assignRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findAllById(
                roleIds != null ? roleIds : List.of()
        );

        user.getRoles().addAll(roles);
        return userRepository.save(user);
    }

    // ----------------------------------------------------
    // Remove roles from user
    // ----------------------------------------------------
    public User removeRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findAllById(
                roleIds != null ? roleIds : List.of()
        );

        user.getRoles().removeAll(roles);
        return userRepository.save(user);
    }
}
