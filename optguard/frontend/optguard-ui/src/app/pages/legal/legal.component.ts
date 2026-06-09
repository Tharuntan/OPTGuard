import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-privacy',
  standalone: true,
  imports: [RouterLink],
  template: `
    <main class="legal-page">
      <section class="legal-card">
        <a class="brand text-decoration-none" routerLink="/">OPTGuard</a>
        <p class="eyebrow mt-4">Last updated June 2026</p>
        <h1>Privacy Policy</h1>
        <p>
          OPTGuard stores the account and OPT/STEM OPT planning information you enter so the app can help you organize deadlines,
          employer records, and reminder checklists.
        </p>
        <h2>Information we collect</h2>
        <p>Account details, student profile fields, OPT/STEM OPT dates, employer records, deadline status, and DSO template activity you save in the app.</p>
        <h2>How we use it</h2>
        <p>We use your information to provide the OPTGuard dashboard, generate reminders, keep your records available after login, and improve the product during beta.</p>
        <h2>What we do not do</h2>
        <p>We do not sell your personal information. OPTGuard is not a legal service and does not replace guidance from your DSO or immigration attorney.</p>
        <h2>Beta notice</h2>
        <p>This is an early public beta. Avoid storing highly sensitive documents or information that is not needed for deadline tracking.</p>
        <div class="legal-actions">
          <a routerLink="/terms">Terms</a>
          <a routerLink="/register">Create account</a>
        </div>
      </section>
    </main>
  `
})
export class PrivacyComponent {
}

@Component({
  selector: 'app-terms',
  standalone: true,
  imports: [RouterLink],
  template: `
    <main class="legal-page">
      <section class="legal-card">
        <a class="brand text-decoration-none" routerLink="/">OPTGuard</a>
        <p class="eyebrow mt-4">Last updated June 2026</p>
        <h1>Terms of Use</h1>
        <p>
          OPTGuard is a productivity tool for organizing OPT and STEM OPT dates, reminders, employer records, and DSO email templates.
        </p>
        <h2>No legal advice</h2>
        <p>OPTGuard does not provide legal advice. You are responsible for confirming all immigration requirements and deadlines with your DSO or immigration attorney.</p>
        <h2>Your responsibility</h2>
        <p>You are responsible for entering accurate dates, reviewing generated reminders, and taking required actions on time.</p>
        <h2>Beta availability</h2>
        <p>The app may change during beta and may have downtime on free hosting infrastructure. Keep your own records outside OPTGuard.</p>
        <h2>Acceptable use</h2>
        <p>Do not misuse the service, attempt unauthorized access, or upload content you do not have the right to use.</p>
        <div class="legal-actions">
          <a routerLink="/privacy">Privacy</a>
          <a routerLink="/register">Create account</a>
        </div>
      </section>
    </main>
  `
})
export class TermsComponent {
}
