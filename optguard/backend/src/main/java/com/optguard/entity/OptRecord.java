package com.optguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "opt_records")
public class OptRecord extends AuditableEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    private LocalDate optStartDate;
    private LocalDate optEndDate;
    private LocalDate stemStartDate;
    private LocalDate stemEndDate;
    private LocalDate eadStartDate;
    private LocalDate eadEndDate;
    private Integer unemploymentDaysUsed = 0;
    private String status;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public LocalDate getOptStartDate() {
        return optStartDate;
    }

    public void setOptStartDate(LocalDate optStartDate) {
        this.optStartDate = optStartDate;
    }

    public LocalDate getOptEndDate() {
        return optEndDate;
    }

    public void setOptEndDate(LocalDate optEndDate) {
        this.optEndDate = optEndDate;
    }

    public LocalDate getStemStartDate() {
        return stemStartDate;
    }

    public void setStemStartDate(LocalDate stemStartDate) {
        this.stemStartDate = stemStartDate;
    }

    public LocalDate getStemEndDate() {
        return stemEndDate;
    }

    public void setStemEndDate(LocalDate stemEndDate) {
        this.stemEndDate = stemEndDate;
    }

    public LocalDate getEadStartDate() {
        return eadStartDate;
    }

    public void setEadStartDate(LocalDate eadStartDate) {
        this.eadStartDate = eadStartDate;
    }

    public LocalDate getEadEndDate() {
        return eadEndDate;
    }

    public void setEadEndDate(LocalDate eadEndDate) {
        this.eadEndDate = eadEndDate;
    }

    public Integer getUnemploymentDaysUsed() {
        return unemploymentDaysUsed;
    }

    public void setUnemploymentDaysUsed(Integer unemploymentDaysUsed) {
        this.unemploymentDaysUsed = unemploymentDaysUsed == null ? 0 : unemploymentDaysUsed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
