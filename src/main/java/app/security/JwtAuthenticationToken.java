package app.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final List<String> permissions;

    public JwtAuthenticationToken(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities,
            List<String> permissions
    ) {
        super(principal, credentials, authorities);
        this.permissions = permissions;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
