import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ApiService } from '../../core/api.service';
import { EmailTemplate } from '../../core/models';

@Component({
  selector: 'app-email-templates',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">DSO email templates</p>
        <h1>Prepared messages with placeholders</h1>
      </div>
    </section>

    <section class="split-grid align-start">
      <div class="panel">
        <div class="template-list">
          <button class="template-button" type="button" *ngFor="let template of templates" (click)="selected = template" [class.active]="selected?.id === template.id">
            <strong>{{ template.title }}</strong>
            <span>{{ template.templateType }}</span>
          </button>
        </div>
      </div>

      <article class="panel template-preview" *ngIf="selected as template">
        <div class="panel-header">
          <h2>{{ template.title }}</h2>
          <button class="btn btn-sm btn-outline-secondary" type="button" (click)="copy(template)">Copy</button>
        </div>
        <label class="form-field mb-3">
          <span>Subject</span>
          <input class="form-control" [value]="template.subject" readonly>
        </label>
        <label class="form-field">
          <span>Body</span>
          <textarea class="form-control template-body" rows="16" [value]="template.body" readonly></textarea>
        </label>
        <p class="text-success small mt-3 mb-0" *ngIf="copied">Copied to clipboard</p>
      </article>
    </section>
  `
})
export class EmailTemplatesComponent implements OnInit {
  private readonly api = inject(ApiService);

  templates: EmailTemplate[] = [];
  selected: EmailTemplate | null = null;
  copied = false;

  ngOnInit(): void {
    this.api.emailTemplates().subscribe((templates) => {
      this.templates = templates;
      this.selected = templates[0] ?? null;
    });
  }

  copy(template: EmailTemplate): void {
    const text = `Subject: ${template.subject}\n\n${template.body}`;
    if (typeof navigator !== 'undefined' && navigator.clipboard) {
      void navigator.clipboard.writeText(text).then(() => {
        this.copied = true;
        window.setTimeout(() => this.copied = false, 2000);
      });
    }
  }
}
