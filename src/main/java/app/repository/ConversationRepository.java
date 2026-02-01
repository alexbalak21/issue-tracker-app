package app.repository;

import app.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    // Optional helper: find conversation by ticket ID
    Conversation findByTicketId(Long ticketId);
}
