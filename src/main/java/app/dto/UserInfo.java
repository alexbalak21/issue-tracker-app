package app.dto;

import app.model.Role;
import app.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UserInfo {

    private Long id;
    private String name;
    private String email;
    private List<String> roles;
    private String createdAt;
    private String updatedAt;

    // Construct from CustomUserDetails (recommended)
    public UserInfo(CustomUserDetails user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles(); // already returns List<String>
        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
        this.updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null;
    }

    // Construct from Spring Security's User (rarely used)
    public UserInfo(org.springframework.security.core.userdetails.User springUser) {
        this.id = null;
        this.name = null;
        this.email = springUser.getUsername();
        this.roles = springUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        this.createdAt = null;
        this.updatedAt = null;
    }

    // Construct from app.model.User entity (RBAC version)
    public UserInfo(app.model.User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();

        // Convert Set<Role> → List<String>
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
        this.updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}
