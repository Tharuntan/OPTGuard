import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ApiService } from '../../core/api.service';
import { Deadline } from '../../core/models';

@Component({
  selector: 'app-deadlines',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">Deadlines</p>
        <h1>Generated reminders and reporting checklists</h1>
      </div>
      <button class="btn btn-teal" type="button" (click)="generate()" [disabled]="loading">Generate reminders</button>
    </section>

    <div class="panel">
      <div class="table-responsive">
        <table class="table align-middle">
          <thead>
            <tr>
              <th>Title</th>
              <th>Type</th>
              <th>Due date</th>
              <th>Priority</th>
              <th>Status</th>
              <th class="text-end">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let deadline of deadlines">
              <td>
                <strong>{{ deadline.title }}</strong>
                <div class="text-muted small">{{ deadline.description }}</div>
              </td>
              <td>{{ deadline.deadlineType }}</td>
              <td>{{ deadline.dueDate || 'Checklist item' }}</td>
              <td><span class="badge-soft" [ngClass]="deadline.priority.toLowerCase()">{{ deadline.priority }}</span></td>
              <td><span class="badge-soft" [ngClass]="deadline.status === 'COMPLETED' ? 'safe' : 'pending'">{{ deadline.status }}</span></td>
              <td class="text-end">
                <button class="btn btn-sm btn-outline-success" type="button" *ngIf="deadline.status === 'PENDING'" (click)="complete(deadline)">Complete</button>
                <button class="btn btn-sm btn-outline-secondary" type="button" *ngIf="deadline.status === 'COMPLETED'" (click)="reopen(deadline)">Pending</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="empty-state" *ngIf="deadlines.length === 0">No deadlines yet. Add OPT/STEM dates, then generate reminders.</div>
    </div>
  `
})
export class DeadlinesComponent implements OnInit {
  private readonly api = inject(ApiService);

  deadlines: Deadline[] = [];
  loading = false;

  ngOnInit(): void {
    this.load();
  }

  generate(): void {
    this.loading = true;
    this.api.generateDeadlines().subscribe({
      next: (deadlines) => {
        this.deadlines = deadlines;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  complete(deadline: Deadline): void {
    this.api.completeDeadline(deadline.id).subscribe(() => this.load());
  }

  reopen(deadline: Deadline): void {
    this.api.reopenDeadline(deadline.id).subscribe(() => this.load());
  }

  private load(): void {
    this.api.deadlines().subscribe((deadlines) => {
      this.deadlines = deadlines;
    });
  }
}
