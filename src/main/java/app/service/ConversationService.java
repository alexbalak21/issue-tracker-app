package app.service;

import app.model.Conversation;
import app.model.Ticket;
import app.repository.ConversationRepository;
import app.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final TicketRepository ticketRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               TicketRepository ticketRepository) {
        this.conversationRepository = conversationRepository;
        this.ticketRepository = ticketRepository;
    }

    public Conversation getConversationById(int id) {
        return conversationRepository.findById(id).orElse(null);
    }

    public Conversation getByTicketId(Long ticketId) {
        return conversationRepository.findByTicketId(ticketId);
    }

    public Conversation createConversationForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Conversation conv = new Conversation();
        conv.setTicket(ticket);
        conv.setCreatedAt(LocalDateTime.now());
        conv.setUpdatedAt(LocalDateTime.now());

        return conversationRepository.save(conv);
    }
}
