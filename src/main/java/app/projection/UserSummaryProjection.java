package app.projection;

import java.time.LocalDateTime;
import java.util.List;

public interface UserSummaryProjection {
    Long getId();
    String getName();
    String getEmail();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    List<String> getRoleNames();
}
