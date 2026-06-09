package com.optguard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "deadlines")
public class Deadline extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeadlineType deadlineType;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeadlineStatus status = DeadlineStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeadlinePriority priority = DeadlinePriority.SAFE;

    private boolean generated;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public DeadlineType getDeadlineType() {
        return deadlineType;
    }

    public void setDeadlineType(DeadlineType deadlineType) {
        this.deadlineType = deadlineType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public DeadlineStatus getStatus() {
        return status;
    }

    public void setStatus(DeadlineStatus status) {
        this.status = status;
    }

    public DeadlinePriority getPriority() {
        return priority;
    }

    public void setPriority(DeadlinePriority priority) {
        this.priority = priority;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }
}
