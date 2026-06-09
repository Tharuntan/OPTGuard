import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">Account</p>
        <h1>Manage your OPTGuard account</h1>
      </div>
    </section>

    <div class="alert alert-success" *ngIf="success">{{ success }}</div>
    <div class="alert alert-danger" *ngIf="error">{{ error }}</div>

    <section class="split-grid align-start">
      <article class="panel">
        <div class="panel-header">
          <h2>Profile name</h2>
        </div>
        <form [formGroup]="nameForm" (ngSubmit)="saveName()" class="vstack gap-3">
          <label class="form-field">
            <span>Full name</span>
            <input class="form-control" type="text" formControlName="fullName" autocomplete="name">
          </label>
          <button class="btn btn-teal" type="submit" [disabled]="nameForm.invalid || savingName">
            {{ savingName ? 'Saving...' : 'Save name' }}
          </button>
        </form>
      </article>

      <article class="panel">
        <div class="panel-header">
          <h2>Password</h2>
        </div>
        <form [formGroup]="passwordForm" (ngSubmit)="changePassword()" class="vstack gap-3">
          <label class="form-field">
            <span>Current password</span>
            <input class="form-control" type="password" formControlName="currentPassword" autocomplete="current-password">
          </label>
          <label class="form-field">
            <span>New password</span>
            <input class="form-control" type="password" formControlName="newPassword" autocomplete="new-password">
          </label>
          <button class="btn btn-teal" type="submit" [disabled]="passwordForm.invalid || savingPassword">
            {{ savingPassword ? 'Updating...' : 'Change password' }}
          </button>
        </form>
      </article>
    </section>

    <section class="panel danger-panel mt-4">
      <div>
        <p class="eyebrow">Danger zone</p>
        <h2>Delete account</h2>
        <p class="warning-copy">
          This permanently removes your account, profile, OPT/STEM record, generated deadlines, and employer records.
        </p>
      </div>
      <form [formGroup]="deleteForm" (ngSubmit)="deleteAccount()" class="delete-form">
        <label class="form-field">
          <span>Type DELETE to confirm</span>
          <input class="form-control" type="text" formControlName="confirmation" autocomplete="off">
        </label>
        <button class="btn btn-outline-danger" type="submit" [disabled]="deleteForm.invalid || deleting">
          {{ deleting ? 'Deleting...' : 'Delete my account' }}
        </button>
      </form>
    </section>
  `
})
export class AccountComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  savingName = false;
  savingPassword = false;
  deleting = false;
  success = '';
  error = '';

  nameForm = this.fb.nonNullable.group({
    fullName: [this.auth.currentUser()?.fullName ?? '', [Validators.required]]
  });

  passwordForm = this.fb.nonNullable.group({
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.required, Validators.minLength(8)]]
  });

  deleteForm = this.fb.nonNullable.group({
    confirmation: ['', [Validators.required, Validators.pattern(/^DELETE$/)]]
  });

  saveName(): void {
    if (this.nameForm.invalid) {
      return;
    }
    this.resetMessages();
    this.savingName = true;
    this.auth.updateAccount(this.nameForm.getRawValue()).subscribe({
      next: () => {
        this.success = 'Account name updated.';
        this.savingName = false;
      },
      error: (response: unknown) => {
        this.error = accountErrorMessage(response, 'Could not update account name.');
        this.savingName = false;
      }
    });
  }

  changePassword(): void {
    if (this.passwordForm.invalid) {
      return;
    }
    this.resetMessages();
    this.savingPassword = true;
    this.auth.changePassword(this.passwordForm.getRawValue()).subscribe({
      next: () => {
        this.success = 'Password updated.';
        this.passwordForm.reset();
        this.savingPassword = false;
      },
      error: (response: unknown) => {
        this.error = accountErrorMessage(response, 'Could not update password.');
        this.savingPassword = false;
      }
    });
  }

  deleteAccount(): void {
    if (this.deleteForm.invalid) {
      return;
    }
    this.resetMessages();
    this.deleting = true;
    this.auth.deleteAccount().subscribe({
      next: () => void this.router.navigate(['/']),
      error: (response: unknown) => {
        this.error = accountErrorMessage(response, 'Could not delete account.');
        this.deleting = false;
      }
    });
  }

  private resetMessages(): void {
    this.success = '';
    this.error = '';
  }
}

function accountErrorMessage(response: unknown, fallback: string): string {
  if (response instanceof HttpErrorResponse && typeof response.error?.message === 'string') {
    return response.error.message;
  }
  return fallback;
}
