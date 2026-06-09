package com.optguard.service;

import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.DeadlinePriority;
import com.optguard.entity.DeadlineStatus;
import com.optguard.entity.DeadlineType;
import com.optguard.entity.OptRecord;
import com.optguard.entity.StudentProfile;
import com.optguard.repository.DeadlineRepository;
import com.optguard.repository.OptRecordRepository;
import com.optguard.repository.StudentProfileRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeadlineGenerationService {
    private static final int[] REMINDER_DAYS = {90, 60, 30, 7};

    private final DeadlineRepository deadlineRepository;
    private final OptRecordRepository optRecordRepository;
    private final StudentProfileRepository studentProfileRepository;

    public DeadlineGenerationService(
            DeadlineRepository deadlineRepository,
            OptRecordRepository optRecordRepository,
            StudentProfileRepository studentProfileRepository
    ) {
        this.deadlineRepository = deadlineRepository;
        this.optRecordRepository = optRecordRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @Transactional
    public List<Deadline> generateForUser(AppUser user) {
        OptRecord record = optRecordRepository.findByUser(user).orElse(null);
        StudentProfile profile = studentProfileRepository.findByUser(user).orElse(null);
        List<Deadline> generated = new ArrayList<>();

        if (record != null) {
            generated.addAll(generateStemDeadlines(user, record));
            generated.addAll(generateRollingReminders(
                    user,
                    record.getEadEndDate(),
                    DeadlineType.EAD_EXPIRY_REMINDER,
                    "EAD expiry reminder",
                    "Your EAD end date is approaching. Confirm renewal or reporting requirements with your DSO."
            ));
            generated.addAll(generateRollingReminders(
                    user,
                    record.getOptEndDate(),
                    DeadlineType.OPT_END_REMINDER,
                    "OPT end reminder",
                    "Your initial OPT end date is approaching. Confirm next steps with your DSO."
            ));
            generated.addAll(generateRollingReminders(
                    user,
                    record.getStemEndDate(),
                    DeadlineType.STEM_END_REMINDER,
                    "STEM OPT end reminder",
                    "Your STEM OPT end date is approaching. Confirm final reporting requirements with your DSO."
            ));
        }

        if (profile != null && profile.getPassportExpiryDate() != null) {
            generated.addAll(generateRollingReminders(
                    user,
                    profile.getPassportExpiryDate(),
                    DeadlineType.PASSPORT_EXPIRY_REMINDER,
                    "Passport expiry reminder",
                    "Your passport expiry date is approaching. Keep travel and status documents current."
            ));
        }

        generated.add(createIfMissing(
                user,
                DeadlineType.EMPLOYER_CHANGE_REPORTING,
                "Employer change reporting checklist",
                "When your employer changes, report it promptly and confirm the required timeline with your DSO.",
                null
        ));
        generated.add(createIfMissing(
                user,
                DeadlineType.ADDRESS_CHANGE_REPORTING,
                "Address change reporting checklist",
                "When your address changes, report it promptly and confirm the required timeline with your DSO.",
                null
        ));

        return generated.stream()
                .filter(deadline -> deadline.getId() != null)
                .sorted(Comparator.comparing(Deadline::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    private List<Deadline> generateStemDeadlines(AppUser user, OptRecord record) {
        List<Deadline> deadlines = new ArrayList<>();
        LocalDate stemStart = record.getStemStartDate();
        LocalDate stemEnd = record.getStemEndDate();
        if (stemStart == null || stemEnd == null) {
            return deadlines;
        }

        // OPT/STEM OPT rules can change. Verify these calculations regularly against official government and DSO guidance.
        LocalDate validationDate = stemStart.plusMonths(6);
        int month = 6;
        while (!validationDate.isAfter(stemEnd)) {
            deadlines.add(createIfMissing(
                    user,
                    DeadlineType.STEM_VALIDATION,
                    month + "-month STEM OPT validation",
                    "Submit or confirm the required STEM OPT validation report with your DSO.",
                    validationDate
            ));
            validationDate = validationDate.plusMonths(6);
            month += 6;
        }

        deadlines.add(createIfMissing(
                user,
                DeadlineType.STEM_12_MONTH_EVALUATION,
                "12-month STEM OPT self-evaluation",
                "Complete the 12-month I-983 self-evaluation and coordinate submission with your DSO.",
                stemStart.plusMonths(12)
        ));
        deadlines.add(createIfMissing(
                user,
                DeadlineType.STEM_18_MONTH_VALIDATION,
                "18-month STEM OPT validation",
                "Submit or confirm the 18-month STEM OPT validation report with your DSO.",
                stemStart.plusMonths(18)
        ));
        deadlines.add(createIfMissing(
                user,
                DeadlineType.STEM_FINAL_EVALUATION,
                "24-month final STEM OPT evaluation",
                "Complete the final I-983 evaluation and confirm submission requirements with your DSO.",
                stemEnd
        ));

        return deadlines;
    }

    private List<Deadline> generateRollingReminders(
            AppUser user,
            LocalDate endDate,
            DeadlineType type,
            String baseTitle,
            String description
    ) {
        List<Deadline> deadlines = new ArrayList<>();
        if (endDate == null) {
            return deadlines;
        }
        for (int days : REMINDER_DAYS) {
            deadlines.add(createIfMissing(
                    user,
                    type,
                    baseTitle + " - " + days + " days before",
                    description,
                    endDate.minusDays(days)
            ));
        }
        return deadlines;
    }

    private Deadline createIfMissing(
            AppUser user,
            DeadlineType type,
            String title,
            String description,
            LocalDate dueDate
    ) {
        if (deadlineRepository.existsByUserAndDeadlineTypeAndTitleAndDueDate(user, type, title, dueDate)) {
            Deadline skipped = new Deadline();
            return skipped;
        }
        Deadline deadline = new Deadline();
        deadline.setUser(user);
        deadline.setDeadlineType(type);
        deadline.setTitle(title);
        deadline.setDescription(description);
        deadline.setDueDate(dueDate);
        deadline.setStatus(DeadlineStatus.PENDING);
        deadline.setPriority(priorityFor(dueDate));
        deadline.setGenerated(true);
        return deadlineRepository.save(deadline);
    }

    public DeadlinePriority priorityFor(LocalDate dueDate) {
        if (dueDate == null) {
            return DeadlinePriority.SAFE;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        if (days <= 14) {
            return DeadlinePriority.URGENT;
        }
        if (days <= 30) {
            return DeadlinePriority.WARNING;
        }
        return DeadlinePriority.SAFE;
    }
}
