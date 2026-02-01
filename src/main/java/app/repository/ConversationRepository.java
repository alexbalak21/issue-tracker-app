package app.repository;

import app.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Conversation ID == Ticket ID, so this is enough:
    // findById(ticketId)
}
