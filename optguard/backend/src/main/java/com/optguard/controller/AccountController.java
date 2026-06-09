package com.optguard.controller;

import com.optguard.dto.ApiDtos.AccountUpdateRequest;
import com.optguard.dto.ApiDtos.ChangePasswordRequest;
import com.optguard.dto.ApiDtos.MessageResponse;
import com.optguard.dto.ApiDtos.UserResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.exception.BadRequestException;
import com.optguard.repository.DeadlineRepository;
import com.optguard.repository.EmployerRepository;
import com.optguard.repository.OptRecordRepository;
import com.optguard.repository.StudentProfileRepository;
import com.optguard.repository.UserRepository;
import com.optguard.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final OptRecordRepository optRecordRepository;
    private final DeadlineRepository deadlineRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountController(
            CurrentUserService currentUserService,
            UserRepository userRepository,
            StudentProfileRepository profileRepository,
            OptRecordRepository optRecordRepository,
            DeadlineRepository deadlineRepository,
            EmployerRepository employerRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.optRecordRepository = optRecordRepository;
        this.deadlineRepository = deadlineRepository;
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping
    @Transactional
    public UserResponse updateAccount(@Valid @RequestBody AccountUpdateRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        user.setFullName(request.fullName().trim());
        return ApiMappers.toUser(userRepository.save(user));
    }

    @PutMapping("/password")
    @Transactional
    public MessageResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (request.newPassword().length() < 8) {
            throw new BadRequestException("New password must be at least 8 characters");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return new MessageResponse("Password updated");
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deleteAccount() {
        AppUser user = currentUserService.getCurrentUser();
        deadlineRepository.deleteByUser(user);
        employerRepository.deleteByUser(user);
        optRecordRepository.deleteByUser(user);
        profileRepository.deleteByUser(user);
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
