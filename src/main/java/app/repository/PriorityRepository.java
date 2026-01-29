package app.repository;

import app.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    Optional<Priority> findByName(String name);
}