package app.repository;

import app.model.Ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // User-owned tickets
    List<Ticket> findByCreatedBy(Long userId);

    // Assigned tickets
    List<Ticket> findByAssignedTo(Long userId);

    // Filters
    List<Ticket> findByStatusId(int statusId);
    List<Ticket> findByPriorityId(int priorityId);

    // Combined filters
    List<Ticket> findByCreatedByAndStatusId(Long userId, int statusId);
    List<Ticket> findByAssignedToAndStatusId(Long userId, int statusId);

    // Search
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);

    List<Ticket> findByCreatedByOrAssignedTo(Long createdBy, Long assignedTo);

}
