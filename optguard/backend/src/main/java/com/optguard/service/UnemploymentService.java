package com.optguard.service;

import com.optguard.entity.OptRecord;
import org.springframework.stereotype.Service;

@Service
public class UnemploymentService {
    public static final int INITIAL_OPT_MAX_DAYS = 90;
    public static final int STEM_OPT_AGGREGATE_MAX_DAYS = 150;

    public int maxAllowedDays(OptRecord record) {
        boolean hasStem = record != null && (record.getStemStartDate() != null || record.getStemEndDate() != null);
        return hasStem ? STEM_OPT_AGGREGATE_MAX_DAYS : INITIAL_OPT_MAX_DAYS;
    }

    public int remainingDays(OptRecord record) {
        if (record == null) {
            return INITIAL_OPT_MAX_DAYS;
        }
        return Math.max(0, maxAllowedDays(record) - safeUsed(record));
    }

    public String warningMessage(OptRecord record) {
        int remaining = remainingDays(record);
        if (remaining < 10) {
            return "Urgent: unemployment days remaining are below 10. Confirm your situation with your DSO or immigration attorney.";
        }
        if (remaining < 30) {
            return "Warning: unemployment days remaining are below 30. Confirm your situation with your DSO or immigration attorney.";
        }
        return "No unemployment day warning based on the information entered.";
    }

    private int safeUsed(OptRecord record) {
        return record.getUnemploymentDaysUsed() == null ? 0 : record.getUnemploymentDaysUsed();
    }
}
