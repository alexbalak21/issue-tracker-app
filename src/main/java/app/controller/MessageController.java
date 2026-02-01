package app.controller;

import app.model.Message;
import app.security.Ownership;
import app.security.OwnershipType;
import app.security.RequiresPermission;
import app.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // ----------------------------------------------------
    // GET messages for a ticket
    // ----------------------------------------------------
    @GetMapping("/{ticketId}/messages")
    @RequiresPermission("conversation.read")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public List<Message> getMessages(@PathVariable Long ticketId) {
        return messageService.getMessagesForTicket(ticketId);
    }

    // ----------------------------------------------------
    // POST message to a ticket
    // ----------------------------------------------------
    @PostMapping("/{ticketId}/messages")
    @RequiresPermission("conversation.reply")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public ResponseEntity<Message> addMessage(
            @PathVariable Long ticketId,
            @RequestBody String body
    ) {
        return ResponseEntity.ok(
                messageService.addMessageToTicket(ticketId, body)
        );
    }
}
