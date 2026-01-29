package app.repository;

import app.model.Ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // User-owned tickets
    List<Ticket> findByCreatedBy(int userId);

    // Assigned tickets
    List<Ticket> findByAssignedTo(Integer userId);

    // Filters
    List<Ticket> findByStatusId(int statusId);
    List<Ticket> findByPriorityId(int priorityId);

    // Combined filters
    List<Ticket> findByCreatedByAndStatusId(int userId, int statusId);
    List<Ticket> findByAssignedToAndStatusId(Integer userId, int statusId);

    // Search
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);
}
