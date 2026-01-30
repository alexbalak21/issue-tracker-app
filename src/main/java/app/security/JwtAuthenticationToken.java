package app.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Long userId;
    private final List<String> permissions;

    public JwtAuthenticationToken(
            Long userId,
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities,
            List<String> permissions
    ) {
        super(principal, credentials, authorities);
        this.userId = userId;
        this.permissions = permissions;
    }

    public Long getUserId() {
        return userId;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
