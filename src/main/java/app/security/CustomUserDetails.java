package app.security;

import app.model.Permission;
import app.model.Role;
import app.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        this.authorities = buildAuthorities(user);
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(User user) {
        return user.getRoles().stream()
                .flatMap(role -> {

                    // ROLE_xxx authority
                    Stream<GrantedAuthority> roleAuth =
                            Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));

                    // permission.xxx authorities
                    Stream<GrantedAuthority> permAuth =
                            role.getPermissions().stream()
                                    .map(Permission::getName)
                                    .map(SimpleGrantedAuthority::new);

                    // Explicit type fixes Java's inference issue
                    return Stream.<GrantedAuthority>concat(roleAuth, permAuth);
                })
                .collect(Collectors.toSet());
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getName() {
        return user.getName();
    }

    public Instant getCreatedAt() {
        return user.getCreatedAt() != null
                ? user.getCreatedAt().toInstant(ZoneOffset.UTC)
                : null;
    }

    public Instant getUpdatedAt() {
        return user.getUpdatedAt() != null
                ? user.getUpdatedAt().toInstant(ZoneOffset.UTC)
                : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Return pure role names
    public List<String> getRoles() {
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }

    // Return pure permission names
    public List<String> getPermissions() {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
