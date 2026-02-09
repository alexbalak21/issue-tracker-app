package app.repository;

import app.model.User;
import app.projection.UserSummaryProjection;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u join u.roles r where r.id = :roleId")
    List<User> findAllByRoleId(Long roleId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = { "roles", "roles.permissions" })
    @Query("select u from User u")
    List<User> findAllWithRoles();

    @Query("""
                select u.id as id,
                       u.name as name,
                       u.email as email,
                       u.createdAt as createdAt,
                       u.updatedAt as updatedAt,
                       r.name as roleNames
                from User u
                left join u.roles r
            """)
    List<UserSummaryProjection> findAllUserSummaries();

}
