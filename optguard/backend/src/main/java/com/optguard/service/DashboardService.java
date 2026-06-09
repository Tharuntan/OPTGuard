package com.optguard.service;

import com.optguard.dto.ApiDtos.DashboardSummaryResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.DeadlinePriority;
import com.optguard.entity.DeadlineStatus;
import com.optguard.entity.OptRecord;
import com.optguard.repository.DeadlineRepository;
import com.optguard.repository.OptRecordRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final OptRecordRepository optRecordRepository;
    private final DeadlineRepository deadlineRepository;
    private final UnemploymentService unemploymentService;

    public DashboardService(
            OptRecordRepository optRecordRepository,
            DeadlineRepository deadlineRepository,
            UnemploymentService unemploymentService
    ) {
        this.optRecordRepository = optRecordRepository;
        this.deadlineRepository = deadlineRepository;
        this.unemploymentService = unemploymentService;
    }

    public DashboardSummaryResponse summary(AppUser user) {
        OptRecord record = optRecordRepository.findByUser(user).orElse(null);
        List<Deadline> upcoming = deadlineRepository
                .findByUserAndStatusAndDueDateGreaterThanEqualOrderByDueDateAsc(
                        user,
                        DeadlineStatus.PENDING,
                        LocalDate.now()
                );

        List<Deadline> urgent = upcoming.stream()
                .filter(deadline -> deadline.getPriority() == DeadlinePriority.URGENT || isDueSoon(deadline))
                .limit(5)
                .toList();

        return new DashboardSummaryResponse(
                record == null ? "PROFILE_INCOMPLETE" : record.getStatus(),
                record == null ? null : record.getOptEndDate(),
                record == null ? null : record.getStemEndDate(),
                record == null ? 0 : record.getUnemploymentDaysUsed(),
                unemploymentService.remainingDays(record),
                upcoming.stream()
                        .sorted(Comparator.comparing(Deadline::getDueDate))
                        .limit(5)
                        .map(ApiMappers::toDeadline)
                        .toList(),
                urgent.stream().map(ApiMappers::toDeadline).toList(),
                unemploymentService.warningMessage(record)
        );
    }

    private boolean isDueSoon(Deadline deadline) {
        if (deadline.getDueDate() == null) {
            return false;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), deadline.getDueDate());
        return days >= 0 && days <= 14;
    }
}
