import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { Employer } from '../../core/models';

@Component({
  selector: 'app-employers',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">Employers</p>
        <h1>Employer and worksite records</h1>
      </div>
    </section>

    <section class="split-grid align-start">
      <form [formGroup]="form" (ngSubmit)="save()" class="panel form-panel">
        <div class="panel-header">
          <h2>{{ editingId ? 'Edit employer' : 'Add employer' }}</h2>
          <button class="btn btn-sm btn-outline-secondary" type="button" *ngIf="editingId" (click)="resetForm()">Cancel</button>
        </div>
        <div class="row g-3">
          <label class="col-md-6 form-field">
            <span>Employer name</span>
            <input class="form-control" formControlName="employerName">
          </label>
          <label class="col-md-6 form-field">
            <span>Job title</span>
            <input class="form-control" formControlName="jobTitle">
          </label>
          <label class="col-md-6 form-field">
            <span>EIN</span>
            <input class="form-control" formControlName="ein">
          </label>
          <label class="col-md-6 form-field">
            <span>E-Verify number</span>
            <input class="form-control" formControlName="eVerifyNumber">
          </label>
          <label class="col-md-6 form-field">
            <span>Employment start date</span>
            <input class="form-control" type="date" formControlName="employmentStartDate">
          </label>
          <label class="col-md-6 form-field">
            <span>Employment end date</span>
            <input class="form-control" type="date" formControlName="employmentEndDate">
          </label>
          <label class="col-md-6 form-field">
            <span>Supervisor name</span>
            <input class="form-control" formControlName="supervisorName">
          </label>
          <label class="col-md-6 form-field">
            <span>Supervisor email</span>
            <input class="form-control" type="email" formControlName="supervisorEmail">
          </label>
          <label class="col-12 form-field">
            <span>Worksite address</span>
            <textarea class="form-control" rows="3" formControlName="worksiteAddress"></textarea>
          </label>
          <label class="form-check ms-2">
            <input class="form-check-input" type="checkbox" formControlName="current">
            <span class="form-check-label">Current employer</span>
          </label>
        </div>
        <div class="d-flex align-items-center gap-3 mt-4">
          <button class="btn btn-teal" type="submit" [disabled]="form.invalid || saving">
            {{ saving ? 'Saving...' : editingId ? 'Update employer' : 'Add employer' }}
          </button>
          <span class="text-danger" *ngIf="error">{{ error }}</span>
        </div>
      </form>

      <div class="panel">
        <div class="panel-header">
          <h2>Saved employers</h2>
          <span class="badge-soft safe">{{ employers.length }}</span>
        </div>
        <div class="empty-state" *ngIf="employers.length === 0">No employers added yet.</div>
        <div class="employer-list" *ngIf="employers.length > 0">
          <article class="employer-row" *ngFor="let employer of employers; trackBy: trackByEmployer">
            <div>
              <strong>{{ employer.employerName }}</strong>
              <span>{{ employer.jobTitle || 'Role not entered' }}</span>
              <span>{{ employer.employmentStartDate || 'Start date not entered' }}</span>
            </div>
            <div class="row-actions">
              <span class="badge-soft safe" *ngIf="employer.current">Current</span>
              <button class="btn btn-sm btn-outline-secondary" type="button" (click)="edit(employer)">Edit</button>
              <button class="btn btn-sm btn-outline-danger" type="button" (click)="remove(employer)">Delete</button>
            </div>
          </article>
        </div>
      </div>
    </section>
  `
})
export class EmployersComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ApiService);

  employers: Employer[] = [];
  editingId: number | null = null;
  saving = false;
  error = '';
  form = this.fb.nonNullable.group({
    employerName: ['', [Validators.required]],
    ein: [''],
    eVerifyNumber: [''],
    jobTitle: [''],
    employmentStartDate: [''],
    employmentEndDate: [''],
    worksiteAddress: [''],
    supervisorName: [''],
    supervisorEmail: ['', [Validators.email]],
    current: [true]
  });

  ngOnInit(): void {
    this.load();
  }

  save(): void {
    if (this.form.invalid) {
      return;
    }
    this.saving = true;
    this.error = '';
    const payload = cleanEmployer(this.form.getRawValue());
    const request = this.editingId
      ? this.api.updateEmployer(this.editingId, payload)
      : this.api.createEmployer(payload);

    request.subscribe({
      next: () => {
        this.saving = false;
        this.resetForm();
        this.load();
      },
      error: () => {
        this.error = 'Unable to save employer.';
        this.saving = false;
      }
    });
  }

  edit(employer: Employer): void {
    this.editingId = employer.id ?? null;
    this.form.patchValue({
      employerName: employer.employerName,
      ein: employer.ein ?? '',
      eVerifyNumber: employer.eVerifyNumber ?? '',
      jobTitle: employer.jobTitle ?? '',
      employmentStartDate: employer.employmentStartDate ?? '',
      employmentEndDate: employer.employmentEndDate ?? '',
      worksiteAddress: employer.worksiteAddress ?? '',
      supervisorName: employer.supervisorName ?? '',
      supervisorEmail: employer.supervisorEmail ?? '',
      current: employer.current
    });
  }

  remove(employer: Employer): void {
    if (!employer.id || !confirm(`Delete ${employer.employerName}?`)) {
      return;
    }
    this.api.deleteEmployer(employer.id).subscribe(() => this.load());
  }

  resetForm(): void {
    this.editingId = null;
    this.form.reset({
      employerName: '',
      ein: '',
      eVerifyNumber: '',
      jobTitle: '',
      employmentStartDate: '',
      employmentEndDate: '',
      worksiteAddress: '',
      supervisorName: '',
      supervisorEmail: '',
      current: true
    });
  }

  trackByEmployer(_: number, employer: Employer): number | string {
    return employer.id ?? employer.employerName;
  }

  private load(): void {
    this.api.employers().subscribe((employers) => {
      this.employers = employers;
    });
  }
}

function cleanEmployer(employer: ReturnType<EmployersComponent['form']['getRawValue']>): Employer {
  return {
    ...employer,
    employmentStartDate: employer.employmentStartDate || undefined,
    employmentEndDate: employer.employmentEndDate || undefined
  };
}
