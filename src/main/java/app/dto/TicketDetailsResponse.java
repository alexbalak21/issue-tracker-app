package app.dto;

import java.util.List;

import app.model.Conversation;
import app.model.Message;
import app.model.Ticket;

public record TicketDetailsResponse(
        Ticket ticket,
        Conversation conversation,
        List<Message> messages,
        List<NoteResponse> notes
) {}
