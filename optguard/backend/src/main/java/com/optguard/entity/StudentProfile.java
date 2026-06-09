package com.optguard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
public class StudentProfile extends AuditableEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    private String schoolName;
    private String dsoName;
    private String dsoEmail;

    @Column(length = 20)
    private String sevisId;

    private String visaStatus;
    private LocalDate passportExpiryDate;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDsoName() {
        return dsoName;
    }

    public void setDsoName(String dsoName) {
        this.dsoName = dsoName;
    }

    public String getDsoEmail() {
        return dsoEmail;
    }

    public void setDsoEmail(String dsoEmail) {
        this.dsoEmail = dsoEmail;
    }

    public String getSevisId() {
        return sevisId;
    }

    public void setSevisId(String sevisId) {
        this.sevisId = sevisId;
    }

    public String getVisaStatus() {
        return visaStatus;
    }

    public void setVisaStatus(String visaStatus) {
        this.visaStatus = visaStatus;
    }

    public LocalDate getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(LocalDate passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }
}
