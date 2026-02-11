package app.service;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.UserBasic;
import app.dto.UserDto;
import app.dto.UserSummary;
import app.model.Role;
import app.model.User;
import app.projection.UserSummaryProjection;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UserService {
                /**
                 * Change the current user's password after verifying the current password
                 * @param username the username (email) of the authenticated user
                 * @param currentPassword the current password
                 * @param newPassword the new password
                 */
                public void changeCurrentUserPassword(String username, String currentPassword, String newPassword) {
                        User user = userRepository.findByEmail(username)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                                throw new RuntimeException("Current password is incorrect");
                        }
                        user.setPassword(passwordEncoder.encode(newPassword));
                        userRepository.save(user);
                }
        /**
         * Update the current user's name and/or email by username (email is unique)
         * @param username the username (email) of the authenticated user
         * @param name new name (nullable)
         * @param email new email (nullable)
         * @return UserBasic with updated info
         */
        public UserBasic updateCurrentUser(String username, String name, String email) {
                User user = userRepository.findByEmail(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                boolean changed = false;
                if (name != null && !name.isBlank() && !name.equals(user.getName())) {
                        user.setName(name);
                        changed = true;
                }
                if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
                        if (userRepository.existsByEmail(email)) {
                                throw new DataIntegrityViolationException("Email already in use");
                        }
                        user.setEmail(email);
                        changed = true;
                }
                if (changed) {
                        userRepository.save(user);
                }
                return new UserBasic(user.getId(), user.getName());
        }
        // ----------------------------------------------------
        // List all users by role (basic: id and name only)
        // ----------------------------------------------------
        @Transactional(readOnly = true)
        public List<UserBasic> getAllUsersByRole(Long roleId) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                return userRepository.findAll()
                        .stream()
                        .filter(user -> user.getRoles().contains(role))
                        .map(user -> new UserBasic(user.getId(), user.getName()))
                        .toList();
        }

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
    public UserDto create(String name, String email, String password, List<Long> roleIds) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        List<Role> roles = roleRepository.findAllById(
                roleIds != null ? roleIds : Collections.emptyList()
        );

        User user = new User(
                name,
                passwordEncoder.encode(password),
                email,
                new HashSet<>(roles)
        );

        User saved = userRepository.save(user);

        return UserDto.from(saved);
    }

    // ----------------------------------------------------
    // List all users (admin) — SUMMARY DTO
    // ----------------------------------------------------
    @Transactional(readOnly = true)
    public List<UserSummary> getAll() {

        return userRepository.findAllUserSummaries()
                .stream()
                .collect(Collectors.groupingBy(
                        UserSummaryProjection::getId
                ))
                .values()
                .stream()
                .map(list -> {
                    var first = list.get(0);

                    List<String> roles = list.stream()
                            .map(UserSummaryProjection::getRoleNames)
                            .flatMap(List::stream)
                            .toList();

                    return new UserSummary(
                            first.getId(),
                            first.getName(),
                            first.getEmail(),
                            first.getCreatedAt(),
                            first.getUpdatedAt(),
                            roles
                    );
                })
                .toList();
    }

    // ----------------------------------------------------
    // List all users (basic: id and name only)
    // ----------------------------------------------------
    @Transactional(readOnly = true)
    public List<UserBasic> getAllBasic() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserBasic(user.getId(), user.getName()))
                .toList();
    }

    // ----------------------------------------------------
    // Assign roles to user
    // ----------------------------------------------------
    public UserDto assignRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findAllById(
                roleIds != null ? roleIds : Collections.emptyList()
        );

        user.getRoles().addAll(roles);

        User saved = userRepository.save(user);

        return UserDto.from(saved);
    }

    // ----------------------------------------------------
    // Remove roles from user
    // ----------------------------------------------------
    public UserDto removeRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findAllById(
                roleIds != null ? roleIds : Collections.emptyList()
        );

        user.getRoles().removeAll(roles);

        User saved = userRepository.save(user);

        return UserDto.from(saved);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDto.from(user);
    }
        /**
         * Returns the role name for a given role ID, or throws if not found.
         */
        @Transactional(readOnly = true)
        public String getRoleNameById(Long roleId) {
                return roleRepository.findById(roleId)
                                .map(Role::getName)
                                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        }
}
