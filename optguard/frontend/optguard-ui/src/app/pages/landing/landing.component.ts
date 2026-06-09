import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink],
  template: `
    <main class="landing-page">
      <nav class="landing-nav">
        <a class="brand" routerLink="/">OPTGuard</a>
        <div class="d-flex gap-2">
          <a class="btn btn-outline-light btn-sm" routerLink="/login">Login</a>
          <a class="btn btn-amber btn-sm" routerLink="/register">Get Started</a>
        </div>
      </nav>

      <section class="landing-hero" aria-label="OPTGuard overview">
        <img src="assets/optguard-hero.png" alt="Desk with deadline dashboard, calendar, passport, and checklist">
        <div class="hero-overlay"></div>
        <div class="hero-copy">
          <p class="eyebrow">OPT and STEM OPT deadline tracker</p>
          <h1>Never miss your OPT or STEM OPT deadline again.</h1>
          <p class="lead">
            Track reporting deadlines, I-983 reminders, unemployment days, employer changes, and DSO email templates in one dashboard.
          </p>
          <div class="d-flex flex-wrap gap-3">
            <a class="btn btn-amber btn-lg" routerLink="/register">Get Started</a>
            <a class="btn btn-outline-light btn-lg" routerLink="/login">Login</a>
          </div>
        </div>
      </section>

      <section class="landing-band">
        <div class="container py-4">
          <div class="legal-banner mb-4">
            OPTGuard is not legal advice. Users must confirm deadlines and requirements with their DSO or immigration attorney.
          </div>
          <div class="row g-3">
            <div class="col-md-4">
              <div class="feature-tile">
                <span class="status-dot safe"></span>
                <h2>Deadline visibility</h2>
                <p>Generated reminders for validations, evaluations, EAD expiry, OPT end, STEM end, and checklist-style reporting items.</p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="feature-tile">
                <span class="status-dot warning"></span>
                <h2>Unemployment tracking</h2>
                <p>Simple day-used and day-remaining calculations with warning and urgent states for review.</p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="feature-tile">
                <span class="status-dot urgent"></span>
                <h2>DSO-ready templates</h2>
                <p>Starter email templates for STEM validation, employer changes, address updates, I-983, and portal issues.</p>
              </div>
            </div>
          </div>
          <div class="landing-footer">
            <span>Public beta for OPT/STEM OPT planning.</span>
            <div class="d-flex gap-3">
              <a routerLink="/privacy">Privacy</a>
              <a routerLink="/terms">Terms</a>
            </div>
          </div>
        </div>
      </section>
    </main>
  `
})
export class LandingComponent {
}
