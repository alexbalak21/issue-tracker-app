package app.controller;

import app.dto.AssignTicketRequest;
import app.dto.CreateTicketRequest;
import app.dto.PatchPriorityRequest;
import app.dto.PatchPriorityResponse;
import app.dto.UpdateTicketRequest;
import app.model.Ticket;
import app.security.Ownership;
import app.security.OwnershipType;
import app.security.RequiresPermission;
import app.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ----------------------------------------------------
    // READ — requires ticket.read (must have permission to see all)
    // Regular users only see tickets they created or are assigned to
    // Support/Admin have ticket.read permission so they see all
    // ----------------------------------------------------
    @GetMapping
    @RequiresPermission("ticket.read")
    @Ownership(OwnershipType.ALL)
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // ----------------------------------------------------
    // READ DETAILS — requires ticket.read (ALL) or OWN (SELF)
    // ----------------------------------------------------
    @GetMapping("/{id}")
    @RequiresPermission("ticket.read")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public ResponseEntity<?> getTicketDetails(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketDetails(id));
    }

    // ----------------------------------------------------
    // SEARCH / FILTER — still requires ticket.read
    // OwnershipAspect will enforce ALL_OR_SELF automatically
    // ----------------------------------------------------
    @GetMapping("/status/{statusId}")
    @RequiresPermission("ticket.read")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public List<Ticket> getTicketsByStatusId(@PathVariable int statusId) {
        return ticketService.getTicketsByStatusId(statusId);
    }

    @GetMapping("/priority/{priorityId}")
    @RequiresPermission("ticket.read")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public List<Ticket> getTicketsByPriorityId(@PathVariable int priorityId) {
        return ticketService.getTicketsByPriorityId(priorityId);
    }

    @GetMapping("/search")
    @RequiresPermission("ticket.read")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public List<Ticket> searchTicketsByTitle(@RequestParam String keyword) {
        return ticketService.searchTicketsByTitle(keyword);
    }

    // ----------------------------------------------------
    // CREATE — requires ticket.write
    // USER does NOT have ticket.write, but creation is allowed
    // because you give USER the ticket.create permission
    // ----------------------------------------------------
    @PostMapping
    @RequiresPermission("ticket.create")
    public Ticket createTicket(@RequestBody CreateTicketRequest req) {
        return ticketService.createTicket(req.title(), req.body(), req.priorityId());
    }

    // ----------------------------------------------------
    // UPDATE — requires ticket.write OR SELF ownership
    // ----------------------------------------------------
    @PutMapping("/{id}")
    @RequiresPermission("ticket.write")
    @Ownership(OwnershipType.SELF)
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long id,
            @RequestBody UpdateTicketRequest req) {
        return ticketService.getTicketById(id)
                .map(existing -> {
                    Ticket updated = ticketService.updateTicketFields(
                            id,
                            req.title(),
                            req.body(),
                            req.priorityId());
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @PatchMapping("/{id}")
    @RequiresPermission("ticket.write")
    @Ownership(OwnershipType.SELF)
    public ResponseEntity<PatchPriorityResponse> patchPriority(
            @PathVariable Long id,
            @RequestBody PatchPriorityRequest req) {
        Ticket updated = ticketService.updateTicketPriority(id, req.getPriorityId());
        PatchPriorityResponse response = new PatchPriorityResponse(updated.getPriorityId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    // ----------------------------------------------------
    // DELETE — requires ticket.delete (admin/manager only)
    // ----------------------------------------------------
    // TO IMPLEMENT

    // ----------------------------------------------------
    // ASSIGN — requires ticket.assign (support/manager/admin)
    // ----------------------------------------------------
    @PostMapping("/{id}/assign")
    @RequiresPermission("ticket.assign")
    public ResponseEntity<Ticket> assignTicket(
            @PathVariable Long id,
            @RequestBody AssignTicketRequest req) {

        return ResponseEntity.ok(ticketService.assignTicket(id, req.userId()));
    }
}
