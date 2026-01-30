package app.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserSummary(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> roles
) {
    public static UserSummary from(Long id,
                                   String name,
                                   String email,
                                   LocalDateTime createdAt,
                                   LocalDateTime updatedAt,
                                   List<String> roles) {
        return new UserSummary(id, name, email, createdAt, updatedAt, roles);
    }
}
