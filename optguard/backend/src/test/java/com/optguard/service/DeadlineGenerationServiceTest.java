package com.optguard.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.DeadlineType;
import com.optguard.entity.OptRecord;
import com.optguard.repository.DeadlineRepository;
import com.optguard.repository.OptRecordRepository;
import com.optguard.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DeadlineGenerationServiceTest {
    @Autowired
    private DeadlineGenerationService deadlineGenerationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OptRecordRepository optRecordRepository;

    @Autowired
    private DeadlineRepository deadlineRepository;

    @Test
    void generatesStemEvaluationsAndExpiryRemindersWithoutDuplicates() {
        AppUser user = new AppUser();
        user.setEmail("deadline-test@example.com");
        user.setFullName("Deadline Test");
        user.setPasswordHash("hashed");
        user = userRepository.save(user);

        OptRecord record = new OptRecord();
        record.setUser(user);
        record.setOptStartDate(LocalDate.of(2026, 1, 1));
        record.setOptEndDate(LocalDate.of(2026, 12, 31));
        record.setStemStartDate(LocalDate.of(2027, 1, 1));
        record.setStemEndDate(LocalDate.of(2028, 12, 31));
        record.setEadStartDate(LocalDate.of(2026, 1, 1));
        record.setEadEndDate(LocalDate.of(2028, 12, 31));
        record.setUnemploymentDaysUsed(12);
        record.setStatus("STEM_OPT");
        optRecordRepository.save(record);

        deadlineGenerationService.generateForUser(user);
        deadlineGenerationService.generateForUser(user);

        List<Deadline> deadlines = deadlineRepository.findByUserOrderByDueDateAsc(user);

        assertThat(deadlines)
                .extracting(Deadline::getDeadlineType)
                .contains(
                        DeadlineType.STEM_VALIDATION,
                        DeadlineType.STEM_12_MONTH_EVALUATION,
                        DeadlineType.STEM_18_MONTH_VALIDATION,
                        DeadlineType.STEM_FINAL_EVALUATION,
                        DeadlineType.EAD_EXPIRY_REMINDER,
                        DeadlineType.OPT_END_REMINDER,
                        DeadlineType.STEM_END_REMINDER,
                        DeadlineType.EMPLOYER_CHANGE_REPORTING,
                        DeadlineType.ADDRESS_CHANGE_REPORTING
                );
        assertThat(deadlines)
                .filteredOn(deadline -> deadline.getTitle().equals("EAD expiry reminder - 90 days before"))
                .hasSize(1);
    }
}
