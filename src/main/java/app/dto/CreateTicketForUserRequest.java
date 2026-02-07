package app.dto;

public record CreateTicketForUserRequest(
        String title,
        String body,
        int priorityId,
        Long createdByUserId
) {}
