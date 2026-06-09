package com.optguard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class ApiDtos {
    private ApiDtos() {
    }

    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank String password,
            @NotBlank String fullName
    ) {
    }

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {
    }

    public record AuthResponse(String token, UserResponse user) {
    }

    public record UserResponse(Long id, String email, String fullName, Instant createdAt) {
    }

    public record StudentProfileRequest(
            String schoolName,
            String dsoName,
            @Email String dsoEmail,
            String sevisId,
            String visaStatus,
            LocalDate passportExpiryDate
    ) {
    }

    public record StudentProfileResponse(
            Long id,
            String schoolName,
            String dsoName,
            String dsoEmail,
            String sevisId,
            String visaStatus,
            LocalDate passportExpiryDate,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record OptRecordRequest(
            LocalDate optStartDate,
            LocalDate optEndDate,
            LocalDate stemStartDate,
            LocalDate stemEndDate,
            LocalDate eadStartDate,
            LocalDate eadEndDate,
            @Min(0) Integer unemploymentDaysUsed,
            String status
    ) {
    }

    public record OptRecordResponse(
            Long id,
            LocalDate optStartDate,
            LocalDate optEndDate,
            LocalDate stemStartDate,
            LocalDate stemEndDate,
            LocalDate eadStartDate,
            LocalDate eadEndDate,
            Integer unemploymentDaysUsed,
            Integer unemploymentDaysRemaining,
            String status,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record EmployerRequest(
            @NotBlank String employerName,
            String ein,
            String eVerifyNumber,
            String jobTitle,
            LocalDate employmentStartDate,
            LocalDate employmentEndDate,
            String worksiteAddress,
            String supervisorName,
            @Email String supervisorEmail,
            boolean current
    ) {
    }

    public record EmployerResponse(
            Long id,
            String employerName,
            String ein,
            String eVerifyNumber,
            String jobTitle,
            LocalDate employmentStartDate,
            LocalDate employmentEndDate,
            String worksiteAddress,
            String supervisorName,
            String supervisorEmail,
            boolean current,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record DeadlineResponse(
            Long id,
            String deadlineType,
            String title,
            String description,
            LocalDate dueDate,
            String status,
            String priority,
            boolean generated,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record EmailTemplateResponse(
            Long id,
            String templateType,
            String title,
            String subject,
            String body
    ) {
    }

    public record DashboardSummaryResponse(
            String currentStatus,
            LocalDate optEndDate,
            LocalDate stemEndDate,
            Integer unemploymentDaysUsed,
            Integer unemploymentDaysRemaining,
            List<DeadlineResponse> upcomingDeadlines,
            List<DeadlineResponse> urgentDeadlines,
            String warningMessage
    ) {
    }
}
