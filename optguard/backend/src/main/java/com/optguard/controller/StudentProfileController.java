package com.optguard.controller;

import com.optguard.dto.ApiDtos.StudentProfileRequest;
import com.optguard.dto.ApiDtos.StudentProfileResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.entity.StudentProfile;
import com.optguard.exception.BadRequestException;
import com.optguard.repository.StudentProfileRepository;
import com.optguard.service.CurrentUserService;
import com.optguard.service.DeadlineGenerationService;
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
@RequestMapping("/api/profile")
public class StudentProfileController {
    private final CurrentUserService currentUserService;
    private final StudentProfileRepository profileRepository;
    private final DeadlineGenerationService deadlineGenerationService;

    public StudentProfileController(
            CurrentUserService currentUserService,
            StudentProfileRepository profileRepository,
            DeadlineGenerationService deadlineGenerationService
    ) {
        this.currentUserService = currentUserService;
        this.profileRepository = profileRepository;
        this.deadlineGenerationService = deadlineGenerationService;
    }

    @GetMapping
    public ResponseEntity<StudentProfileResponse> getProfile() {
        AppUser user = currentUserService.getCurrentUser();
        return profileRepository.findByUser(user)
                .map(ApiMappers::toProfile)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping
    @Transactional
    public StudentProfileResponse createProfile(@Valid @RequestBody StudentProfileRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (profileRepository.findByUser(user).isPresent()) {
            throw new BadRequestException("Profile already exists. Use PUT to update it.");
        }
        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        apply(request, profile);
        StudentProfile saved = profileRepository.save(profile);
        deadlineGenerationService.generateForUser(user);
        return ApiMappers.toProfile(saved);
    }

    @PutMapping
    @Transactional
    public StudentProfileResponse updateProfile(@Valid @RequestBody StudentProfileRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        StudentProfile profile = profileRepository.findByUser(user).orElseGet(() -> {
            StudentProfile created = new StudentProfile();
            created.setUser(user);
            return created;
        });
        apply(request, profile);
        StudentProfile saved = profileRepository.save(profile);
        deadlineGenerationService.generateForUser(user);
        return ApiMappers.toProfile(saved);
    }

    private void apply(StudentProfileRequest request, StudentProfile profile) {
        profile.setSchoolName(request.schoolName());
        profile.setDsoName(request.dsoName());
        profile.setDsoEmail(request.dsoEmail());
        profile.setSevisId(request.sevisId());
        profile.setVisaStatus(request.visaStatus());
        profile.setPassportExpiryDate(request.passportExpiryDate());
    }
}
