package com.optguard.config;

import com.optguard.entity.AppUser;
import com.optguard.entity.EmailTemplate;
import com.optguard.entity.Employer;
import com.optguard.entity.OptRecord;
import com.optguard.entity.StudentProfile;
import com.optguard.repository.EmailTemplateRepository;
import com.optguard.repository.EmployerRepository;
import com.optguard.repository.OptRecordRepository;
import com.optguard.repository.StudentProfileRepository;
import com.optguard.repository.UserRepository;
import com.optguard.service.DeadlineGenerationService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {
    private final boolean seedDemoData;
    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final OptRecordRepository optRecordRepository;
    private final EmployerRepository employerRepository;
    private final EmailTemplateRepository emailTemplateRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeadlineGenerationService deadlineGenerationService;

    public DataSeeder(
            @Value("${app.seed-demo-data}") boolean seedDemoData,
            UserRepository userRepository,
            StudentProfileRepository profileRepository,
            OptRecordRepository optRecordRepository,
            EmployerRepository employerRepository,
            EmailTemplateRepository emailTemplateRepository,
            PasswordEncoder passwordEncoder,
            DeadlineGenerationService deadlineGenerationService
    ) {
        this.seedDemoData = seedDemoData;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.optRecordRepository = optRecordRepository;
        this.employerRepository = employerRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.passwordEncoder = passwordEncoder;
        this.deadlineGenerationService = deadlineGenerationService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedEmailTemplates();
        if (seedDemoData) {
            seedDemoUser();
        }
    }

    private void seedDemoUser() {
        AppUser user = userRepository.findByEmail("demo@optguard.test").orElseGet(() -> {
            AppUser created = new AppUser();
            created.setEmail("demo@optguard.test");
            created.setFullName("Demo Student");
            created.setPasswordHash(passwordEncoder.encode("Password123!"));
            return userRepository.save(created);
        });

        if (profileRepository.findByUser(user).isEmpty()) {
            StudentProfile profile = new StudentProfile();
            profile.setUser(user);
            profile.setSchoolName("Sample University");
            profile.setDsoName("Jordan DSO");
            profile.setDsoEmail("dso@example.edu");
            profile.setSevisId("N0000000000");
            profile.setVisaStatus("F-1");
            profile.setPassportExpiryDate(LocalDate.now().plusMonths(15));
            profileRepository.save(profile);
        }

        if (optRecordRepository.findByUser(user).isEmpty()) {
            OptRecord record = new OptRecord();
            record.setUser(user);
            record.setOptStartDate(LocalDate.now().minusMonths(7));
            record.setOptEndDate(LocalDate.now().plusMonths(5));
            record.setStemStartDate(LocalDate.now().plusMonths(5).plusDays(1));
            record.setStemEndDate(LocalDate.now().plusMonths(29));
            record.setEadStartDate(record.getOptStartDate());
            record.setEadEndDate(record.getStemEndDate());
            record.setUnemploymentDaysUsed(42);
            record.setStatus("INITIAL_OPT");
            optRecordRepository.save(record);
        }

        if (employerRepository.findByUserOrderByEmploymentStartDateDesc(user).isEmpty()) {
            Employer employer = new Employer();
            employer.setUser(user);
            employer.setEmployerName("Example Analytics Inc.");
            employer.setEin("12-3456789");
            employer.seteVerifyNumber("1234567");
            employer.setJobTitle("Data Analyst");
            employer.setEmploymentStartDate(LocalDate.now().minusMonths(3));
            employer.setWorksiteAddress("100 Market Street, New York, NY");
            employer.setSupervisorName("Avery Manager");
            employer.setSupervisorEmail("avery.manager@example.com");
            employer.setCurrent(true);
            employerRepository.save(employer);
        }

        deadlineGenerationService.generateForUser(user);
    }

    private void seedEmailTemplates() {
        List<EmailTemplateSeed> seeds = List.of(
                new EmailTemplateSeed(
                        "STEM_6_MONTH_VALIDATION",
                        "6-month STEM validation email to DSO",
                        "STEM OPT 6-month validation for {{studentName}}",
                        """
                        Dear {{dsoName}},

                        I am writing to submit my 6-month STEM OPT validation information.

                        Student name: {{studentName}}
                        SEVIS ID: {{sevisId}}
                        School: {{schoolName}}
                        Employer: {{employerName}}
                        Start date: {{startDate}}

                        Please let me know if you need any additional information.

                        Thank you,
                        {{studentName}}
                        """
                ),
                new EmailTemplateSeed(
                        "EMPLOYER_CHANGE",
                        "Employer change email to DSO",
                        "OPT/STEM OPT employer change for {{studentName}}",
                        """
                        Dear {{dsoName}},

                        I am writing to report an employer change and confirm the required next steps.

                        Student name: {{studentName}}
                        SEVIS ID: {{sevisId}}
                        New employer: {{employerName}}
                        Employment start date: {{startDate}}

                        Please confirm if I should submit any additional documentation.

                        Thank you,
                        {{studentName}}
                        """
                ),
                new EmailTemplateSeed(
                        "ADDRESS_CHANGE",
                        "Address change email to DSO",
                        "Address update for {{studentName}}",
                        """
                        Dear {{dsoName}},

                        I am writing to report an address update for my F-1 record.

                        Student name: {{studentName}}
                        SEVIS ID: {{sevisId}}
                        School: {{schoolName}}

                        Please confirm that this update has been received and let me know if I should take any additional action.

                        Thank you,
                        {{studentName}}
                        """
                ),
                new EmailTemplateSeed(
                        "I983_SUBMISSION",
                        "I-983 submission email",
                        "I-983 submission for {{studentName}}",
                        """
                        Dear {{dsoName}},

                        I am submitting my completed Form I-983 for review.

                        Student name: {{studentName}}
                        SEVIS ID: {{sevisId}}
                        Employer: {{employerName}}
                        Start date: {{startDate}}

                        Please let me know if anything is missing or needs correction.

                        Thank you,
                        {{studentName}}
                        """
                ),
                new EmailTemplateSeed(
                        "FINAL_EVALUATION",
                        "Final evaluation submission email",
                        "Final STEM OPT evaluation for {{studentName}}",
                        """
                        Dear {{dsoName}},

                        I am submitting my final STEM OPT evaluation for your review.

                        Student name: {{studentName}}
                        SEVIS ID: {{sevisId}}
                        Employer: {{employerName}}

                        Please confirm receipt and let me know if any additional action is required.

                        Thank you,
                        {{studentName}}
                        """
                ),
                new EmailTemplateSeed(
                        "SEVP_PORTAL_ISSUE",
                        "SEVP portal issue email",
                        "SEVP portal issue for {{studentName}}",
                        """
                        Dear {{dsoName}},

                        I am experiencing an issue with the SEVP Portal and would appreciate guidance.

                        Student name: {{studentName}}
                        SEVIS ID: {{sevisId}}
                        School: {{schoolName}}

                        Please let me know the recommended next steps.

                        Thank you,
                        {{studentName}}
                        """
                )
        );

        for (EmailTemplateSeed seed : seeds) {
            if (emailTemplateRepository.findByTemplateType(seed.templateType()).isEmpty()) {
                EmailTemplate template = new EmailTemplate();
                template.setTemplateType(seed.templateType());
                template.setTitle(seed.title());
                template.setSubject(seed.subject());
                template.setBody(seed.body().strip());
                emailTemplateRepository.save(template);
            }
        }
    }

    private record EmailTemplateSeed(String templateType, String title, String subject, String body) {
    }
}
