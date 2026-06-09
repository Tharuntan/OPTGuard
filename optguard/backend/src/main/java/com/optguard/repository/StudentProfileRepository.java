package com.optguard.repository;

import com.optguard.entity.AppUser;
import com.optguard.entity.StudentProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(AppUser user);

    void deleteByUser(AppUser user);
}
