package com.optguard.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.optguard.entity.OptRecord;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UnemploymentServiceTest {
    private final UnemploymentService service = new UnemploymentService();

    @Test
    void initialOptUsesNinetyDayLimit() {
        OptRecord record = new OptRecord();
        record.setUnemploymentDaysUsed(61);

        assertThat(service.maxAllowedDays(record)).isEqualTo(90);
        assertThat(service.remainingDays(record)).isEqualTo(29);
        assertThat(service.warningMessage(record)).contains("below 30");
    }

    @Test
    void stemOptUsesAggregateOneHundredFiftyDayLimit() {
        OptRecord record = new OptRecord();
        record.setStemStartDate(LocalDate.now());
        record.setUnemploymentDaysUsed(141);

        assertThat(service.maxAllowedDays(record)).isEqualTo(150);
        assertThat(service.remainingDays(record)).isEqualTo(9);
        assertThat(service.warningMessage(record)).contains("below 10");
    }
}
