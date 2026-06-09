package com.optguard.controller;

import com.optguard.dto.ApiDtos.DeadlineResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.DeadlineStatus;
import com.optguard.exception.NotFoundException;
import com.optguard.repository.DeadlineRepository;
import com.optguard.service.CurrentUserService;
import com.optguard.service.DeadlineGenerationService;
import java.util.Comparator;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deadlines")
public class DeadlineController {
    private final CurrentUserService currentUserService;
    private final DeadlineRepository deadlineRepository;
    private final DeadlineGenerationService deadlineGenerationService;

    public DeadlineController(
            CurrentUserService currentUserService,
            DeadlineRepository deadlineRepository,
            DeadlineGenerationService deadlineGenerationService
    ) {
        this.currentUserService = currentUserService;
        this.deadlineRepository = deadlineRepository;
        this.deadlineGenerationService = deadlineGenerationService;
    }

    @GetMapping
    public List<DeadlineResponse> all() {
        AppUser user = currentUserService.getCurrentUser();
        return deadlineRepository.findByUserOrderByDueDateAsc(user)
                .stream()
                .sorted(Comparator.comparing(Deadline::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(ApiMappers::toDeadline)
                .toList();
    }

    @PostMapping("/generate")
    public List<DeadlineResponse> generate() {
        AppUser user = currentUserService.getCurrentUser();
        deadlineGenerationService.generateForUser(user);
        return all();
    }

    @PutMapping("/{id}/complete")
    @Transactional
    public DeadlineResponse complete(@PathVariable Long id) {
        return setStatus(id, DeadlineStatus.COMPLETED);
    }

    @PutMapping("/{id}/pending")
    @Transactional
    public DeadlineResponse pending(@PathVariable Long id) {
        return setStatus(id, DeadlineStatus.PENDING);
    }

    private DeadlineResponse setStatus(Long id, DeadlineStatus status) {
        AppUser user = currentUserService.getCurrentUser();
        Deadline deadline = deadlineRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Deadline not found"));
        deadline.setStatus(status);
        return ApiMappers.toDeadline(deadlineRepository.save(deadline));
    }
}
