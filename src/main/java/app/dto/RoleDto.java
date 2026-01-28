package app.dto;

import java.util.List;
import app.model.Role;

public record RoleDto(
        Long id,
        String name,
        String description,
        List<PermissionDto> permissions
) {
    public static RoleDto from(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getPermissions().stream()
                        .map(PermissionDto::from)
                        .toList()
        );
    }
}
