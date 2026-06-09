import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <main class="auth-page">
      <section class="auth-panel">
        <a class="brand text-decoration-none" routerLink="/">OPTGuard</a>
        <h1>Welcome back</h1>
        <p class="text-muted">Use your OPTGuard account to continue tracking deadlines.</p>

        <form [formGroup]="form" (ngSubmit)="submit()" class="vstack gap-3">
          <label class="form-field">
            <span>Email</span>
            <input class="form-control" type="email" formControlName="email" autocomplete="email">
          </label>
          <label class="form-field">
            <span>Password</span>
            <input class="form-control" type="password" formControlName="password" autocomplete="current-password">
          </label>
          <div class="alert alert-danger py-2" *ngIf="error">{{ error }}</div>
          <button class="btn btn-teal w-100" type="submit" [disabled]="form.invalid || loading">
            {{ loading ? 'Signing in...' : 'Login' }}
          </button>
        </form>

        <p class="mt-3 mb-0 small">New here? <a routerLink="/register">Create an account</a></p>
        <p class="small text-muted mt-3 mb-0">Demo login: demo&#64;optguard.test / Password123!</p>
      </section>
    </main>
  `
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  loading = false;
  error = '';
  form = this.fb.nonNullable.group({
    email: ['demo@optguard.test', [Validators.required, Validators.email]],
    password: ['Password123!', [Validators.required]]
  });

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.error = '';
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => void this.router.navigate(['/app/dashboard']),
      error: () => {
        this.error = 'Login failed. Check your email and password.';
        this.loading = false;
      }
    });
  }
}
