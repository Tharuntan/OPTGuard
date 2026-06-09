import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { ShellComponent } from './layout/shell.component';
import { ChecklistComponent } from './pages/checklist/checklist.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { DeadlinesComponent } from './pages/deadlines/deadlines.component';
import { EmailTemplatesComponent } from './pages/email-templates/email-templates.component';
import { EmployersComponent } from './pages/employers/employers.component';
import { LandingComponent } from './pages/landing/landing.component';
import { PrivacyComponent, TermsComponent } from './pages/legal/legal.component';
import { LoginComponent } from './pages/login/login.component';
import { OptRecordComponent } from './pages/opt-record/opt-record.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'privacy', component: PrivacyComponent },
  { path: 'terms', component: TermsComponent },
  {
    path: 'app',
    component: ShellComponent,
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'profile', component: ProfileComponent },
      { path: 'opt-record', component: OptRecordComponent },
      { path: 'employers', component: EmployersComponent },
      { path: 'deadlines', component: DeadlinesComponent },
      { path: 'email-templates', component: EmailTemplatesComponent },
      { path: 'checklist', component: ChecklistComponent }
    ]
  },
  { path: '**', redirectTo: '' }
];
