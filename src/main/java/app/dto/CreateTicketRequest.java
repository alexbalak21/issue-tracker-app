package app.dto;

public record CreateTicketRequest(
        String title,
        String body,
        int priorityId
) {}
