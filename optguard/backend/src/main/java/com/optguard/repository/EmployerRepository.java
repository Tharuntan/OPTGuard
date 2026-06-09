package com.optguard.repository;

import com.optguard.entity.AppUser;
import com.optguard.entity.Employer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    List<Employer> findByUserOrderByEmploymentStartDateDesc(AppUser user);

    Optional<Employer> findFirstByUserAndCurrentTrueOrderByEmploymentStartDateDesc(AppUser user);

    Optional<Employer> findByIdAndUser(Long id, AppUser user);
}
