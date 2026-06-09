package com.optguard.dto;

import com.optguard.dto.ApiDtos.DeadlineResponse;
import com.optguard.dto.ApiDtos.EmailTemplateResponse;
import com.optguard.dto.ApiDtos.EmployerResponse;
import com.optguard.dto.ApiDtos.OptRecordResponse;
import com.optguard.dto.ApiDtos.StudentProfileResponse;
import com.optguard.dto.ApiDtos.UserResponse;
import com.optguard.entity.AppUser;
import com.optguard.entity.Deadline;
import com.optguard.entity.EmailTemplate;
import com.optguard.entity.Employer;
import com.optguard.entity.OptRecord;
import com.optguard.entity.StudentProfile;

public final class ApiMappers {
    private ApiMappers() {
    }

    public static UserResponse toUser(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getCreatedAt());
    }

    public static StudentProfileResponse toProfile(StudentProfile profile) {
        return new StudentProfileResponse(
                profile.getId(),
                profile.getSchoolName(),
                profile.getDsoName(),
                profile.getDsoEmail(),
                profile.getSevisId(),
                profile.getVisaStatus(),
                profile.getPassportExpiryDate(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    public static OptRecordResponse toOptRecord(OptRecord record, int unemploymentDaysRemaining) {
        return new OptRecordResponse(
                record.getId(),
                record.getOptStartDate(),
                record.getOptEndDate(),
                record.getStemStartDate(),
                record.getStemEndDate(),
                record.getEadStartDate(),
                record.getEadEndDate(),
                record.getUnemploymentDaysUsed(),
                unemploymentDaysRemaining,
                record.getStatus(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    public static EmployerResponse toEmployer(Employer employer) {
        return new EmployerResponse(
                employer.getId(),
                employer.getEmployerName(),
                employer.getEin(),
                employer.geteVerifyNumber(),
                employer.getJobTitle(),
                employer.getEmploymentStartDate(),
                employer.getEmploymentEndDate(),
                employer.getWorksiteAddress(),
                employer.getSupervisorName(),
                employer.getSupervisorEmail(),
                employer.isCurrent(),
                employer.getCreatedAt(),
                employer.getUpdatedAt()
        );
    }

    public static DeadlineResponse toDeadline(Deadline deadline) {
        return new DeadlineResponse(
                deadline.getId(),
                deadline.getDeadlineType().name(),
                deadline.getTitle(),
                deadline.getDescription(),
                deadline.getDueDate(),
                deadline.getStatus().name(),
                deadline.getPriority().name(),
                deadline.isGenerated(),
                deadline.getCreatedAt(),
                deadline.getUpdatedAt()
        );
    }

    public static EmailTemplateResponse toEmailTemplate(EmailTemplate template) {
        return new EmailTemplateResponse(
                template.getId(),
                template.getTemplateType(),
                template.getTitle(),
                template.getSubject(),
                template.getBody()
        );
    }
}
