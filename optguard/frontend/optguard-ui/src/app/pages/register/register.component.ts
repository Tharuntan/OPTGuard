import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <main class="auth-page">
      <section class="auth-panel">
        <a class="brand text-decoration-none" routerLink="/">OPTGuard</a>
        <h1>Create your account</h1>
        <p class="text-muted">Start with your profile, then add OPT/STEM dates to generate reminders.</p>

        <form [formGroup]="form" (ngSubmit)="submit()" class="vstack gap-3">
          <label class="form-field">
            <span>Full name</span>
            <input class="form-control" type="text" formControlName="fullName" autocomplete="name">
          </label>
          <label class="form-field">
            <span>Email</span>
            <input class="form-control" type="email" formControlName="email" autocomplete="email">
          </label>
          <label class="form-field">
            <span>Password</span>
            <input class="form-control" type="password" formControlName="password" autocomplete="new-password">
          </label>
          <div class="alert alert-danger py-2" *ngIf="error">{{ error }}</div>
          <button class="btn btn-teal w-100" type="submit" [disabled]="form.invalid || loading">
            {{ loading ? 'Creating account...' : 'Get Started' }}
          </button>
        </form>

        <p class="mt-3 mb-0 small">Already have an account? <a routerLink="/login">Login</a></p>
      </section>
    </main>
  `
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  loading = false;
  error = '';
  form = this.fb.nonNullable.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.error = '';
    this.auth.register(this.form.getRawValue()).subscribe({
      next: () => void this.router.navigate(['/app/dashboard']),
      error: (response: unknown) => {
        this.error = authErrorMessage(response, 'Registration failed. Please check your details and try again.');
        this.loading = false;
      }
    });
  }
}

function authErrorMessage(response: unknown, fallback: string): string {
  if (response instanceof HttpErrorResponse && typeof response.error?.message === 'string') {
    return response.error.message;
  }
  return fallback;
}
