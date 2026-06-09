package com.optguard.controller;

import com.optguard.dto.ApiDtos.OptRecordRequest;
import com.optguard.dto.ApiDtos.OptRecordResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.entity.OptRecord;
import com.optguard.exception.BadRequestException;
import com.optguard.repository.OptRecordRepository;
import com.optguard.service.CurrentUserService;
import com.optguard.service.DeadlineGenerationService;
import com.optguard.service.UnemploymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/opt-record")
public class OptRecordController {
    private final CurrentUserService currentUserService;
    private final OptRecordRepository optRecordRepository;
    private final DeadlineGenerationService deadlineGenerationService;
    private final UnemploymentService unemploymentService;

    public OptRecordController(
            CurrentUserService currentUserService,
            OptRecordRepository optRecordRepository,
            DeadlineGenerationService deadlineGenerationService,
            UnemploymentService unemploymentService
    ) {
        this.currentUserService = currentUserService;
        this.optRecordRepository = optRecordRepository;
        this.deadlineGenerationService = deadlineGenerationService;
        this.unemploymentService = unemploymentService;
    }

    @GetMapping
    public ResponseEntity<OptRecordResponse> getRecord() {
        AppUser user = currentUserService.getCurrentUser();
        return optRecordRepository.findByUser(user)
                .map(record -> ApiMappers.toOptRecord(record, unemploymentService.remainingDays(record)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping
    @Transactional
    public OptRecordResponse createRecord(@Valid @RequestBody OptRecordRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (optRecordRepository.findByUser(user).isPresent()) {
            throw new BadRequestException("OPT record already exists. Use PUT to update it.");
        }
        OptRecord record = new OptRecord();
        record.setUser(user);
        apply(request, record);
        OptRecord saved = optRecordRepository.save(record);
        deadlineGenerationService.generateForUser(user);
        return ApiMappers.toOptRecord(saved, unemploymentService.remainingDays(saved));
    }

    @PutMapping
    @Transactional
    public OptRecordResponse updateRecord(@Valid @RequestBody OptRecordRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        OptRecord record = optRecordRepository.findByUser(user).orElseGet(() -> {
            OptRecord created = new OptRecord();
            created.setUser(user);
            return created;
        });
        apply(request, record);
        OptRecord saved = optRecordRepository.save(record);
        deadlineGenerationService.generateForUser(user);
        return ApiMappers.toOptRecord(saved, unemploymentService.remainingDays(saved));
    }

    private void apply(OptRecordRequest request, OptRecord record) {
        record.setOptStartDate(request.optStartDate());
        record.setOptEndDate(request.optEndDate());
        record.setStemStartDate(request.stemStartDate());
        record.setStemEndDate(request.stemEndDate());
        record.setEadStartDate(request.eadStartDate());
        record.setEadEndDate(request.eadEndDate());
        record.setUnemploymentDaysUsed(request.unemploymentDaysUsed());
        record.setStatus(request.status() == null || request.status().isBlank() ? "INITIAL_OPT" : request.status());
    }
}
