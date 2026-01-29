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
    private final UserService userService; // optional but recommended

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

    public Optional<Ticket> getTicketById(int id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByCreatedBy(int userId) {
        return ticketRepository.findByCreatedBy(userId);
    }

    public List<Ticket> getTicketsByAssignedTo(Integer userId) {
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
    public void deleteTicket(int id) {
        ticketRepository.deleteById(id);
    }

    // ----------------------------------------------------
    // ASSIGN
    // ----------------------------------------------------
    public Ticket assignTicket(int ticketId, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Validate user exists
        if (!userService.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        ticket.setAssignedTo(userId.intValue());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

}
