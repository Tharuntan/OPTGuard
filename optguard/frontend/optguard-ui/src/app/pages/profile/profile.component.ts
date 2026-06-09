import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { StudentProfile } from '../../core/models';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">Student profile</p>
        <h1>School and DSO details</h1>
      </div>
    </section>

    <form [formGroup]="form" (ngSubmit)="save()" class="panel form-panel">
      <div class="row g-3">
        <label class="col-md-6 form-field">
          <span>School name</span>
          <input class="form-control" formControlName="schoolName">
        </label>
        <label class="col-md-6 form-field">
          <span>Visa status</span>
          <input class="form-control" formControlName="visaStatus" placeholder="F-1">
        </label>
        <label class="col-md-6 form-field">
          <span>DSO name</span>
          <input class="form-control" formControlName="dsoName">
        </label>
        <label class="col-md-6 form-field">
          <span>DSO email</span>
          <input class="form-control" type="email" formControlName="dsoEmail">
        </label>
        <label class="col-md-6 form-field">
          <span>SEVIS ID</span>
          <input class="form-control" formControlName="sevisId">
        </label>
        <label class="col-md-6 form-field">
          <span>Passport expiry date</span>
          <input class="form-control" type="date" formControlName="passportExpiryDate">
        </label>
      </div>

      <div class="d-flex align-items-center gap-3 mt-4">
        <button class="btn btn-teal" type="submit" [disabled]="form.invalid || saving">
          {{ saving ? 'Saving...' : 'Save profile' }}
        </button>
        <span class="text-success" *ngIf="saved">Saved</span>
        <span class="text-danger" *ngIf="error">{{ error }}</span>
      </div>
    </form>
  `
})
export class ProfileComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ApiService);

  saving = false;
  saved = false;
  error = '';
  form = this.fb.nonNullable.group({
    schoolName: [''],
    dsoName: [''],
    dsoEmail: ['', [Validators.email]],
    sevisId: [''],
    visaStatus: ['F-1'],
    passportExpiryDate: ['']
  });

  ngOnInit(): void {
    this.api.getProfile().subscribe((profile) => {
      if (profile) {
        this.form.patchValue({
          schoolName: profile.schoolName ?? '',
          dsoName: profile.dsoName ?? '',
          dsoEmail: profile.dsoEmail ?? '',
          sevisId: profile.sevisId ?? '',
          visaStatus: profile.visaStatus ?? 'F-1',
          passportExpiryDate: profile.passportExpiryDate ?? ''
        });
      }
    });
  }

  save(): void {
    if (this.form.invalid) {
      return;
    }
    this.saving = true;
    this.saved = false;
    this.error = '';
    this.api.saveProfile(clean(this.form.getRawValue())).subscribe({
      next: () => {
        this.saved = true;
        this.saving = false;
      },
      error: () => {
        this.error = 'Unable to save profile.';
        this.saving = false;
      }
    });
  }
}

function clean(profile: Record<string, string>): StudentProfile {
  return Object.fromEntries(
    Object.entries(profile).map(([key, value]) => [key, value === '' ? null : value])
  ) as StudentProfile;
}
