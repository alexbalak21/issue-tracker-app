package app.dto;

import java.util.List;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        List<Long> roleIds
) {}
