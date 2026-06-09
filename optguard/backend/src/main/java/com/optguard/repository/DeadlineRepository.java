package com.optguard.repository;

import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.DeadlineStatus;
import com.optguard.entity.DeadlineType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    List<Deadline> findByUserOrderByDueDateAsc(AppUser user);

    List<Deadline> findByUserAndStatusAndDueDateGreaterThanEqualOrderByDueDateAsc(
            AppUser user,
            DeadlineStatus status,
            LocalDate dueDate
    );

    boolean existsByUserAndDeadlineTypeAndTitleAndDueDate(
            AppUser user,
            DeadlineType deadlineType,
            String title,
            LocalDate dueDate
    );

    Optional<Deadline> findByIdAndUser(Long id, AppUser user);

    void deleteByUser(AppUser user);
}
