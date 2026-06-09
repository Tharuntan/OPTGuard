import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  template: `
    <header class="app-header">
      <nav class="navbar navbar-expand-lg">
        <div class="container-fluid px-3 px-lg-4">
          <a class="navbar-brand fw-bold" routerLink="/app/dashboard">OPTGuard</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto gap-lg-1">
              <li class="nav-item"><a class="nav-link" routerLink="/app/dashboard" routerLinkActive="active">Dashboard</a></li>
              <li class="nav-item"><a class="nav-link" routerLink="/app/profile" routerLinkActive="active">Profile</a></li>
              <li class="nav-item"><a class="nav-link" routerLink="/app/opt-record" routerLinkActive="active">OPT/STEM</a></li>
              <li class="nav-item"><a class="nav-link" routerLink="/app/employers" routerLinkActive="active">Employers</a></li>
              <li class="nav-item"><a class="nav-link" routerLink="/app/deadlines" routerLinkActive="active">Deadlines</a></li>
              <li class="nav-item"><a class="nav-link" routerLink="/app/email-templates" routerLinkActive="active">Templates</a></li>
              <li class="nav-item"><a class="nav-link" routerLink="/app/checklist" routerLinkActive="active">I-983</a></li>
            </ul>
            <div class="d-flex align-items-center gap-3">
              <span class="small text-muted d-none d-lg-inline" *ngIf="auth.user$ | async as user">{{ user.fullName }}</span>
              <button class="btn btn-sm btn-outline-danger" type="button" (click)="logout()">Logout</button>
            </div>
          </div>
        </div>
      </nav>
    </header>

    <main class="app-main">
      <div class="container-fluid px-3 px-lg-4 py-4">
        <div class="legal-banner mb-4">
          OPTGuard is an organization tool, not legal advice. Confirm all immigration requirements and deadlines with your DSO or immigration attorney.
        </div>
        <router-outlet />
      </div>
    </main>
  `
})
export class ShellComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  logout(): void {
    this.auth.logout();
    void this.router.navigate(['/login']);
  }
}
