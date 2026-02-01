package app.dto;

import java.time.LocalDateTime;

public record NoteResponse(
        Long id,
        Long ticketId,
        Long createdBy,
        Long updatedBy,
        String body,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
