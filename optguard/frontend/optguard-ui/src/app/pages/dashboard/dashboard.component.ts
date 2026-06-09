import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { DashboardSummary, Deadline } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">Dashboard</p>
        <h1>OPTGuard command center</h1>
      </div>
      <a class="btn btn-teal" routerLink="/app/opt-record">Update OPT/STEM dates</a>
    </section>

    <div class="alert alert-danger" *ngIf="error">{{ error }}</div>
    <div class="loading" *ngIf="loading">Loading dashboard...</div>

    <ng-container *ngIf="summary as item">
      <section class="metric-grid">
        <article class="metric-card">
          <span class="metric-label">Current status</span>
          <strong>{{ item.currentStatus || 'Profile incomplete' }}</strong>
          <span class="badge-soft safe">Safe</span>
        </article>
        <article class="metric-card">
          <span class="metric-label">STEM OPT end date</span>
          <strong>{{ item.stemEndDate || 'Not entered' }}</strong>
          <span class="metric-note">Confirm with your DSO</span>
        </article>
        <article class="metric-card">
          <span class="metric-label">Unemployment days used</span>
          <strong>{{ item.unemploymentDaysUsed }}</strong>
          <span class="metric-note">Based on your input</span>
        </article>
        <article class="metric-card">
          <span class="metric-label">Remaining unemployment days</span>
          <strong>{{ item.unemploymentDaysRemaining }}</strong>
          <span class="badge-soft" [ngClass]="remainingClass(item.unemploymentDaysRemaining)">
            {{ remainingLabel(item.unemploymentDaysRemaining) }}
          </span>
        </article>
      </section>

      <section class="split-grid mt-4">
        <div class="panel">
          <div class="panel-header">
            <h2>Upcoming deadlines</h2>
            <a routerLink="/app/deadlines">View all</a>
          </div>
          <div class="empty-state" *ngIf="item.upcomingDeadlines.length === 0">
            Add your OPT/STEM dates to generate reminders.
          </div>
          <ul class="deadline-list" *ngIf="item.upcomingDeadlines.length > 0">
            <li *ngFor="let deadline of item.upcomingDeadlines">
              <div>
                <strong>{{ deadline.title }}</strong>
                <span>{{ deadline.dueDate || 'Checklist item' }}</span>
              </div>
              <span class="badge-soft" [ngClass]="priorityClass(deadline)">{{ deadline.priority }}</span>
            </li>
          </ul>
        </div>

        <div class="panel">
          <div class="panel-header">
            <h2>Urgent warnings</h2>
            <span class="badge-soft urgent">{{ item.urgentDeadlines.length }}</span>
          </div>
          <p class="warning-copy">{{ item.warningMessage }}</p>
          <ul class="deadline-list compact" *ngIf="item.urgentDeadlines.length > 0">
            <li *ngFor="let deadline of item.urgentDeadlines">
              <div>
                <strong>{{ deadline.title }}</strong>
                <span>{{ deadline.dueDate }}</span>
              </div>
            </li>
          </ul>
        </div>
      </section>
    </ng-container>
  `
})
export class DashboardComponent implements OnInit {
  private readonly api = inject(ApiService);

  summary: DashboardSummary | null = null;
  loading = true;
  error = '';

  ngOnInit(): void {
    this.api.dashboard().subscribe({
      next: (summary) => {
        this.summary = summary;
        this.loading = false;
      },
      error: () => {
        this.error = 'Unable to load dashboard summary.';
        this.loading = false;
      }
    });
  }

  remainingClass(days: number): string {
    if (days < 10) {
      return 'urgent';
    }
    if (days < 30) {
      return 'warning';
    }
    return 'safe';
  }

  remainingLabel(days: number): string {
    if (days < 10) {
      return 'Urgent';
    }
    if (days < 30) {
      return 'Warning';
    }
    return 'Safe';
  }

  priorityClass(deadline: Deadline): string {
    return deadline.priority.toLowerCase();
  }
}
