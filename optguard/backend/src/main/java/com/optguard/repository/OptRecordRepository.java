package com.optguard.repository;

import com.optguard.entity.AppUser;
import com.optguard.entity.OptRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptRecordRepository extends JpaRepository<OptRecord, Long> {
    Optional<OptRecord> findByUser(AppUser user);
}
