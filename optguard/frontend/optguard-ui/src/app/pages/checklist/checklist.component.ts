import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

interface ChecklistSection {
  title: string;
  items: string[];
}

@Component({
  selector: 'app-checklist',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="page-title">
      <div>
        <p class="eyebrow">I-983 checklist</p>
        <h1>Training plan organization</h1>
      </div>
    </section>

    <div class="legal-banner mb-4">
      This checklist is for organization only and is not legal advice. Confirm requirements with your DSO.
    </div>

    <section class="checklist-grid">
      <article class="panel checklist-panel" *ngFor="let section of sections">
        <h2>{{ section.title }}</h2>
        <label class="check-row" *ngFor="let item of section.items">
          <input type="checkbox">
          <span>{{ item }}</span>
        </label>
      </article>
    </section>
  `
})
export class ChecklistComponent {
  sections: ChecklistSection[] = [
    {
      title: 'Section 1: Student information',
      items: ['Name and SEVIS ID reviewed', 'School information reviewed', 'STEM degree information reviewed']
    },
    {
      title: 'Section 2: Student certification',
      items: ['Student certification signed', 'Start date reviewed', 'Student responsibilities reviewed']
    },
    {
      title: 'Section 3: Employer information',
      items: ['Employer name reviewed', 'EIN reviewed', 'E-Verify information reviewed']
    },
    {
      title: 'Section 4: Employer certification',
      items: ['Official with signatory authority identified', 'Compensation and hours reviewed', 'Training resources reviewed']
    },
    {
      title: 'Section 5: Training plan',
      items: ['Learning objectives reviewed', 'Oversight plan reviewed', 'Evaluation methods reviewed']
    },
    {
      title: 'Section 6: Employer official certification',
      items: ['Employer official certification signed', 'Dates reviewed', 'Contact details reviewed']
    },
    {
      title: 'Evaluation on student progress',
      items: ['12-month evaluation drafted', 'Student signature collected', 'Employer signature collected']
    },
    {
      title: 'Final evaluation',
      items: ['Final evaluation drafted', 'Final signatures collected', 'Submission timing confirmed with DSO']
    }
  ];
}
