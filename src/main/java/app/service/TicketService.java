package app.service;

import app.model.Ticket;
import app.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;

    public TicketService(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
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
    public Ticket createTicket(Ticket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    // ----------------------------------------------------
    // UPDATE
    // ----------------------------------------------------
    public Ticket updateTicket(Ticket ticket) {
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    // ----------------------------------------------------
    // DELETE
    // ----------------------------------------------------
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    // ----------------------------------------------------
    // ASSIGN
    // ----------------------------------------------------
    public Ticket assignTicket(Long ticketId, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!userService.existsById(userId)) {
            throw new RuntimeException("User not found");
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
