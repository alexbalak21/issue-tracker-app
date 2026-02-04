package app.service;

import app.model.Conversation;
import app.model.Message;
import app.repository.ConversationRepository;
import app.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AuthService authService;

    public MessageService(MessageRepository messageRepository,
                          ConversationRepository conversationRepository,
                          AuthService authService) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.authService = authService;
    }

    // ----------------------------------------------------
    // ADD MESSAGE TO A TICKET'S CONVERSATION
    // ----------------------------------------------------
    public Message addMessageToTicket(Long ticketId, String body) {

        Long userId = authService.getCurrentUserId();

        Conversation conv = conversationRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Message msg = new Message();
        msg.setConversation(conv);
        msg.setSenderId(userId);
        msg.setBody(body);
        msg.setCreatedAt(LocalDateTime.now());

        Message saved = messageRepository.save(msg);

        // Update conversation metadata
        conv.setUpdatedAt(LocalDateTime.now());
        conv.setLastSenderId(userId);
        conversationRepository.save(conv);

        return saved;
    }

    // ----------------------------------------------------
    // GET MESSAGES FOR A TICKET'S CONVERSATION
    // ----------------------------------------------------
    public List<Message> getMessagesForTicket(Long ticketId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(ticketId);
    }
}
