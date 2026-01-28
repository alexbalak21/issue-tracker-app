package app.security;

import app.model.Permission;
import app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String accessSecret;

    @Value("${app.jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${app.jwt.expiration}")
    private long accessExpirationMs;

    @Value("${app.refresh.expiration}")
    private long refreshExpirationMs;

    // ---------------------------
    // ACCESS TOKEN GENERATION
    // ---------------------------

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        // Basic user info
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());

        // Add permissions from roles
        List<String> permissions = user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .toList();

        claims.put("permissions", permissions);

        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setClaims(claims)
                .setId(jti)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getAccessKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", userDetails.getEmail());
        claims.put("name", userDetails.getName());

        // Permissions from authorities
        List<String> permissions = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("permissions", permissions);

        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setClaims(claims)
                .setId(jti)
                .setSubject(String.valueOf(userDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getAccessKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------------------------
    // REFRESH TOKEN GENERATION
    // ---------------------------

    public String generateRefreshToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return createRefreshToken(claims, String.valueOf(userId));
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setClaims(claims)
                .setId(jti)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getRefreshKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------------------------
    // VALIDATION + CLAIM EXTRACTION
    // ---------------------------

    public Boolean validateToken(String token, Long userId) {
        final String tokenSub = extractSubject(token);
        return tokenSub.equals(String.valueOf(userId)) && !isTokenExpired(token);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ---------------------------
    // PERMISSION EXTRACTION
    // ---------------------------

    public List<String> extractPermissions(String token) {
        return extractClaim(token, claims -> {
            Object raw = claims.get("permissions");
            if (raw instanceof List<?> list) {
                return list.stream().map(Object::toString).toList();
            }
            return List.of();
        });
    }

    // ---------------------------
    // KEYS
    // ---------------------------

    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(accessSecret.getBytes());
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    // ---------------------------
    // REFRESH TOKEN VERIFICATION
    // ---------------------------

    public Claims extractRefreshClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = extractRefreshClaims(token);

            Object type = claims.get("type");
            if (type == null || !"refresh".equals(type.toString())) {
                return false;
            }

            return claims.getExpiration().after(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromRefreshToken(String token) {
        Claims claims = extractRefreshClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public boolean isRefreshTokenExpired(String token) {
        Claims claims = extractRefreshClaims(token);
        return claims.getExpiration().before(new Date());
    }
}
