package app.dto;

public record UpdateTicketRequest(
        String title,
        String body,
        Integer priorityId
) {}
