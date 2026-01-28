package app.dto;

import app.model.Permission;

public record PermissionDto(
        Long id,
        String name,
        String description
) {
    public static PermissionDto from(Permission p) {
        return new PermissionDto(p.getId(), p.getName(), p.getDescription());
    }
}
