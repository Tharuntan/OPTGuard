package com.optguard.controller;

import com.optguard.dto.ApiDtos.EmailTemplateResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.exception.NotFoundException;
import com.optguard.repository.EmailTemplateRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-templates")
public class EmailTemplateController {
    private final EmailTemplateRepository emailTemplateRepository;

    public EmailTemplateController(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @GetMapping
    public List<EmailTemplateResponse> all() {
        return emailTemplateRepository.findAll()
                .stream()
                .map(ApiMappers::toEmailTemplate)
                .toList();
    }

    @GetMapping("/{templateType}")
    public EmailTemplateResponse byType(@PathVariable String templateType) {
        return emailTemplateRepository.findByTemplateType(templateType)
                .map(ApiMappers::toEmailTemplate)
                .orElseThrow(() -> new NotFoundException("Email template not found"));
    }
}
