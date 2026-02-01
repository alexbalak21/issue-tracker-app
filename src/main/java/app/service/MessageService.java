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

    public Message addMessage(int conversationId, String body) {

        Long userId = authService.getCurrentUserId();

        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Message msg = new Message();
        msg.setConversation(conv);
        msg.setSenderId(userId.intValue());
        msg.setBody(body);
        msg.setCreatedAt(LocalDateTime.now());
        msg.setUpdatedAt(LocalDateTime.now());

        return messageRepository.save(msg);
    }

    public List<Message> getMessages(int conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
