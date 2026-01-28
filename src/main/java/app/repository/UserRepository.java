package app.repository;

import app.model.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Check if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if a user exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by email.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Load all users with their roles eagerly.
     *
     * This avoids LazyInitializationException when converting to DTOs.
     */
    @EntityGraph(attributePaths = "roles")
    @Query("select u from User u")
    List<User> findAllWithRoles();
}
