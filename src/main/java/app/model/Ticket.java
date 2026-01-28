package app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(name = "priority_id", nullable = false)
    private int priorityId;

    @Column(name = "status_id", nullable = false)
    private int statusId;

    @Column(name = "created_by", nullable = false)
    private int createdBy;

    @Column(name = "assigned_to")
    private Integer assignedTo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    // Constructors
    public Ticket() {}

    public Ticket(String title, String body, int priorityId, int statusId, int createdBy, Integer assignedTo, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime resolvedAt) {
        this.title = title;
        this.body = body;
        this.priorityId = priorityId;
        this.statusId = statusId;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.resolvedAt = resolvedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public int getPriorityId() { return priorityId; }
    public void setPriorityId(int priorityId) { this.priorityId = priorityId; }

    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
