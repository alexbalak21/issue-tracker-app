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
@RequestMapping("/api/conversations")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // ----------------------------------------------------
    // GET messages for a conversation
    // USER can only view their own conversation
    // ----------------------------------------------------
    @GetMapping("/{conversationId}/messages")
    @RequiresPermission("conversation.read")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public List<Message> getMessages(@PathVariable int conversationId) {
        return messageService.getMessages(conversationId);
    }

    // ----------------------------------------------------
    // POST message (reply)
    // USER can only reply to their own conversation
    // ----------------------------------------------------
    @PostMapping("/{conversationId}/messages")
    @RequiresPermission("conversation.reply")
    @Ownership(OwnershipType.ALL_OR_SELF)
    public ResponseEntity<Message> addMessage(
            @PathVariable int conversationId,
            @RequestBody String body
    ) {
        return ResponseEntity.ok(
                messageService.addMessage(conversationId, body)
        );
    }
}
