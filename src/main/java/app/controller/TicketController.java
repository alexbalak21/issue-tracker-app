package app.controller;

import app.dto.AssignTicketRequest;
import app.dto.CreateTicketRequest;
import app.model.Ticket;
import app.security.RequiresPermission;
import app.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ----------------------------------------------------
    // READ — requires ticket.read
    // ----------------------------------------------------
    @GetMapping
    @RequiresPermission("ticket.read")
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    @RequiresPermission("ticket.read")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/createdBy/{userId}")
    @RequiresPermission("ticket.read")
    public List<Ticket> getTicketsByCreatedBy(@PathVariable Long userId) {
        return ticketService.getTicketsByCreatedBy(userId);
    }

    @GetMapping("/assignedTo/{userId}")
    @RequiresPermission("ticket.read")
    public List<Ticket> getTicketsByAssignedTo(@PathVariable Long userId) {
        return ticketService.getTicketsByAssignedTo(userId);
    }

    @GetMapping("/status/{statusId}")
    @RequiresPermission("ticket.read")
    public List<Ticket> getTicketsByStatusId(@PathVariable int statusId) {
        return ticketService.getTicketsByStatusId(statusId);
    }

    @GetMapping("/priority/{priorityId}")
    @RequiresPermission("ticket.read")
    public List<Ticket> getTicketsByPriorityId(@PathVariable int priorityId) {
        return ticketService.getTicketsByPriorityId(priorityId);
    }

    @GetMapping("/search")
    @RequiresPermission("ticket.read")
    public List<Ticket> searchTicketsByTitle(@RequestParam String keyword) {
        return ticketService.searchTicketsByTitle(keyword);
    }

    // ----------------------------------------------------
    // WRITE — requires ticket.write
    // ----------------------------------------------------
    @PostMapping
    @RequiresPermission("ticket.write")
    public Ticket createTicket(@RequestBody CreateTicketRequest req) {
        return ticketService.createTicket(req.title(), req.body(), req.priorityId());
    }

    @PutMapping("/{id}")
    @RequiresPermission("ticket.write")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        if (!ticketService.getTicketById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ticket.setId(id);
        return ResponseEntity.ok(ticketService.updateTicket(ticket));
    }

    // ----------------------------------------------------
    // DELETE — requires ticket.delete
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    @RequiresPermission("ticket.delete")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (!ticketService.getTicketById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------------------------------
    // ASSIGN — requires ticket.assign
    // ----------------------------------------------------
    @PostMapping("/{id}/assign")
    @RequiresPermission("ticket.assign")
    public ResponseEntity<Ticket> assignTicket(
            @PathVariable Long id,
            @RequestBody AssignTicketRequest req) {
        Ticket updated = ticketService.assignTicket(id, req.userId());
        return ResponseEntity.ok(updated);
    }

}
