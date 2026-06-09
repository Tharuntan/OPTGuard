package com.optguard.repository;

import com.optguard.entity.EmailTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByTemplateType(String templateType);
}
