package com.optguard.controller;

import com.optguard.dto.ApiDtos.EmployerRequest;
import com.optguard.dto.ApiDtos.EmployerResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.entity.Employer;
import com.optguard.exception.NotFoundException;
import com.optguard.repository.EmployerRepository;
import com.optguard.service.CurrentUserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {
    private final CurrentUserService currentUserService;
    private final EmployerRepository employerRepository;

    public EmployerController(CurrentUserService currentUserService, EmployerRepository employerRepository) {
        this.currentUserService = currentUserService;
        this.employerRepository = employerRepository;
    }

    @GetMapping
    public List<EmployerResponse> all() {
        AppUser user = currentUserService.getCurrentUser();
        return employerRepository.findByUserOrderByEmploymentStartDateDesc(user)
                .stream()
                .map(ApiMappers::toEmployer)
                .toList();
    }

    @GetMapping("/current")
    public ResponseEntity<EmployerResponse> current() {
        AppUser user = currentUserService.getCurrentUser();
        return employerRepository.findFirstByUserAndCurrentTrueOrderByEmploymentStartDateDesc(user)
                .map(ApiMappers::toEmployer)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping
    @Transactional
    public EmployerResponse create(@Valid @RequestBody EmployerRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (request.current()) {
            clearCurrent(user);
        }
        Employer employer = new Employer();
        employer.setUser(user);
        apply(request, employer);
        return ApiMappers.toEmployer(employerRepository.save(employer));
    }

    @PutMapping("/{id}")
    @Transactional
    public EmployerResponse update(@PathVariable Long id, @Valid @RequestBody EmployerRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        Employer employer = employerRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Employer not found"));
        if (request.current()) {
            clearCurrent(user);
        }
        apply(request, employer);
        return ApiMappers.toEmployer(employerRepository.save(employer));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        AppUser user = currentUserService.getCurrentUser();
        Employer employer = employerRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Employer not found"));
        employerRepository.delete(employer);
        return ResponseEntity.noContent().build();
    }

    private void apply(EmployerRequest request, Employer employer) {
        employer.setEmployerName(request.employerName());
        employer.setEin(request.ein());
        employer.seteVerifyNumber(request.eVerifyNumber());
        employer.setJobTitle(request.jobTitle());
        employer.setEmploymentStartDate(request.employmentStartDate());
        employer.setEmploymentEndDate(request.employmentEndDate());
        employer.setWorksiteAddress(request.worksiteAddress());
        employer.setSupervisorName(request.supervisorName());
        employer.setSupervisorEmail(request.supervisorEmail());
        employer.setCurrent(request.current());
    }

    private void clearCurrent(AppUser user) {
        employerRepository.findByUserOrderByEmploymentStartDateDesc(user)
                .forEach(existing -> existing.setCurrent(false));
    }
}
