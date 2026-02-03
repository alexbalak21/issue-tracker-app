package app.service;

import app.dto.NoteResponse;
import app.dto.TicketDetailsResponse;
import app.model.Conversation;
import app.model.Message;
import app.model.Ticket;
import app.repository.ConversationRepository;
import app.repository.MessageRepository;
import app.repository.TicketRepository;
import app.security.JwtAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final NoteService noteService;
    private final AuthService authService;

    public TicketService(
            TicketRepository ticketRepository,
            UserService userService,
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            NoteService noteService,
            AuthService authService
    ) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.noteService = noteService;
        this.authService = authService;
    }

    // ----------------------------------------------------
    // READ
    // ----------------------------------------------------
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByCreatedBy(Long userId) {
        return ticketRepository.findByCreatedBy(userId);
    }

    public List<Ticket> getTicketsByAssignedTo(Long userId) {
        return ticketRepository.findByAssignedTo(userId);
    }

    public List<Ticket> getTicketsByStatusId(int statusId) {
        return ticketRepository.findByStatusId(statusId);
    }

    public List<Ticket> getTicketsByPriorityId(int priorityId) {
        return ticketRepository.findByPriorityId(priorityId);
    }

    public List<Ticket> searchTicketsByTitle(String keyword) {
        return ticketRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // ----------------------------------------------------
    // CREATE
    // ----------------------------------------------------
    public Ticket createTicket(String title, String body, int priorityId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            userId = jwtAuth.getUserId();
        }

        if (userId == null) {
            throw new RuntimeException("Cannot determine authenticated user");
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setBody(body);
        ticket.setPriorityId(priorityId);
        ticket.setStatusId(1); // default: Open
        ticket.setCreatedBy(userId);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        // Create conversation with SAME ID as ticket
        Conversation conversation = new Conversation();
        conversation.setId(saved.getId());  // Set ID without attaching the detached ticket
        conversationRepository.save(conversation);

        return saved;
    }

    // ----------------------------------------------------
    // UPDATE
    // ----------------------------------------------------
    public Ticket updateTicket(Ticket ticket) {
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketFields(Long id, String title, String body, Integer priorityId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (title != null) ticket.setTitle(title);
        if (body != null) ticket.setBody(body);
        if (priorityId != null) ticket.setPriorityId(priorityId);

        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketPriority(Long id, int priorityId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setPriorityId(priorityId);
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    // ----------------------------------------------------
    // DETAILS (Ticket + Conversation + Messages + Notes)
    // ----------------------------------------------------
    public TicketDetailsResponse getTicketDetails(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Load conversation (may be empty)
        Conversation conversation = conversationRepository.findById(ticketId)
                .orElse(null);

        List<Message> messages = null;

        if (conversation != null) {
            List<Message> foundMessages = messageRepository.findByConversationIdOrderByCreatedAtAsc(ticketId);

            if (!foundMessages.isEmpty()) {
                messages = foundMessages;
            } else {
                // hide empty conversation
                conversation = null;
            }
        }

        // Notes only if user has note.read
        List<NoteResponse> notes = null;

        if (authService.hasPermission("note.read")) {
            List<NoteResponse> foundNotes = noteService.getNotes(ticketId);

            if (!foundNotes.isEmpty()) {
                notes = foundNotes;
            }
        }

        return new TicketDetailsResponse(ticket, conversation, messages, notes);
    }

    // ----------------------------------------------------
    // ASSIGN
    // ----------------------------------------------------
    public Ticket assignTicket(Long ticketId, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (userId != null) {
            if (!userService.existsById(userId)) {
                throw new RuntimeException("User not found");
            }
        }

        ticket.setAssignedTo(userId);
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    // ----------------------------------------------------
    // OWNERSHIP FILTER
    // ----------------------------------------------------
    public List<Ticket> getTicketsByCreatedByOrAssignedTo(Long userId) {
        return ticketRepository.findByCreatedByOrAssignedTo(userId, userId);
    }
}
