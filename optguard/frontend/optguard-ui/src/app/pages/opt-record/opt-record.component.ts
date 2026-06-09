import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { OptRecord } from '../../core/models';

@Component({
  selector: 'app-opt-record',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">OPT/STEM record</p>
        <h1>Dates and unemployment tracking</h1>
      </div>
    </section>

    <form [formGroup]="form" (ngSubmit)="save()" class="panel form-panel">
      <div class="row g-3">
        <label class="col-md-6 col-lg-3 form-field">
          <span>OPT start date</span>
          <input class="form-control" type="date" formControlName="optStartDate">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>OPT end date</span>
          <input class="form-control" type="date" formControlName="optEndDate">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>STEM OPT start date</span>
          <input class="form-control" type="date" formControlName="stemStartDate">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>STEM OPT end date</span>
          <input class="form-control" type="date" formControlName="stemEndDate">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>EAD start date</span>
          <input class="form-control" type="date" formControlName="eadStartDate">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>EAD end date</span>
          <input class="form-control" type="date" formControlName="eadEndDate">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>Unemployment days used</span>
          <input class="form-control" type="number" min="0" formControlName="unemploymentDaysUsed">
        </label>
        <label class="col-md-6 col-lg-3 form-field">
          <span>Status</span>
          <select class="form-select" formControlName="status">
            <option value="INITIAL_OPT">Initial OPT</option>
            <option value="STEM_OPT">STEM OPT</option>
            <option value="COMPLETED">Completed</option>
          </select>
        </label>
      </div>

      <div class="result-strip mt-4" *ngIf="remainingDays !== null">
        <span>Remaining unemployment days</span>
        <strong>{{ remainingDays }}</strong>
      </div>

      <div class="d-flex align-items-center gap-3 mt-4">
        <button class="btn btn-teal" type="submit" [disabled]="form.invalid || saving">
          {{ saving ? 'Saving...' : 'Save and generate deadlines' }}
        </button>
        <span class="text-success" *ngIf="saved">Saved and reminders generated</span>
        <span class="text-danger" *ngIf="error">{{ error }}</span>
      </div>
    </form>
  `
})
export class OptRecordComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ApiService);

  saving = false;
  saved = false;
  error = '';
  remainingDays: number | null = null;
  form = this.fb.nonNullable.group({
    optStartDate: [''],
    optEndDate: [''],
    stemStartDate: [''],
    stemEndDate: [''],
    eadStartDate: [''],
    eadEndDate: [''],
    unemploymentDaysUsed: [0, [Validators.min(0)]],
    status: ['INITIAL_OPT']
  });

  ngOnInit(): void {
    this.api.getOptRecord().subscribe((record) => {
      if (record) {
        this.remainingDays = record.unemploymentDaysRemaining ?? null;
        this.form.patchValue({
          optStartDate: record.optStartDate ?? '',
          optEndDate: record.optEndDate ?? '',
          stemStartDate: record.stemStartDate ?? '',
          stemEndDate: record.stemEndDate ?? '',
          eadStartDate: record.eadStartDate ?? '',
          eadEndDate: record.eadEndDate ?? '',
          unemploymentDaysUsed: record.unemploymentDaysUsed ?? 0,
          status: record.status ?? 'INITIAL_OPT'
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
    this.api.saveOptRecord(cleanOptRecord(this.form.getRawValue())).subscribe({
      next: (record) => {
        this.remainingDays = record.unemploymentDaysRemaining ?? null;
        this.saved = true;
        this.saving = false;
      },
      error: () => {
        this.error = 'Unable to save OPT/STEM record.';
        this.saving = false;
      }
    });
  }
}

function cleanOptRecord(record: ReturnType<OptRecordComponent['form']['getRawValue']>): OptRecord {
  return {
    ...record,
    optStartDate: record.optStartDate || undefined,
    optEndDate: record.optEndDate || undefined,
    stemStartDate: record.stemStartDate || undefined,
    stemEndDate: record.stemEndDate || undefined,
    eadStartDate: record.eadStartDate || undefined,
    eadEndDate: record.eadEndDate || undefined
  };
}
