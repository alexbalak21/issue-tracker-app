package app.dto;

import java.time.LocalDateTime;
import java.util.List;

import app.model.User;

public record UserDto(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<RoleDto> roles,
        List<String> permissions
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),

                // Convert roles to RoleDto
                user.getRoles().stream()
                        .map(RoleDto::from)
                        .toList(),

                // Flatten permissions across all roles
                user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(p -> p.getName())
                        .distinct()
                        .toList()
        );
    }
}
