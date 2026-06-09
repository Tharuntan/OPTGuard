package com.optguard.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.optguard.dto.ApiDtos.DashboardSummaryResponse;
import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.DeadlinePriority;
import com.optguard.entity.DeadlineStatus;
import com.optguard.entity.DeadlineType;
import com.optguard.entity.OptRecord;
import com.optguard.repository.DeadlineRepository;
import com.optguard.repository.OptRecordRepository;
import com.optguard.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DashboardServiceTest {
    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OptRecordRepository optRecordRepository;

    @Autowired
    private DeadlineRepository deadlineRepository;

    @Test
    void summaryIncludesUnemploymentWarningAndUrgentDeadlines() {
        AppUser user = new AppUser();
        user.setEmail("dashboard-test@example.com");
        user.setFullName("Dashboard Test");
        user.setPasswordHash("hashed");
        user = userRepository.save(user);

        OptRecord record = new OptRecord();
        record.setUser(user);
        record.setStatus("INITIAL_OPT");
        record.setOptEndDate(LocalDate.now().plusMonths(2));
        record.setUnemploymentDaysUsed(82);
        optRecordRepository.save(record);

        Deadline urgent = new Deadline();
        urgent.setUser(user);
        urgent.setDeadlineType(DeadlineType.OPT_END_REMINDER);
        urgent.setTitle("OPT end reminder - 7 days before");
        urgent.setDescription("Confirm next steps with DSO.");
        urgent.setDueDate(LocalDate.now().plusDays(7));
        urgent.setStatus(DeadlineStatus.PENDING);
        urgent.setPriority(DeadlinePriority.URGENT);
        deadlineRepository.save(urgent);

        DashboardSummaryResponse summary = dashboardService.summary(user);

        assertThat(summary.currentStatus()).isEqualTo("INITIAL_OPT");
        assertThat(summary.unemploymentDaysRemaining()).isEqualTo(8);
        assertThat(summary.warningMessage()).contains("below 10");
        assertThat(summary.urgentDeadlines()).hasSize(1);
    }
}
