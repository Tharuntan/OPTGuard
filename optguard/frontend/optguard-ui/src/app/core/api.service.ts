import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { DashboardSummary, Deadline, EmailTemplate, Employer, OptRecord, StudentProfile } from './models';
import { API_URL } from './runtime-config';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = inject(API_URL);

  dashboard(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.apiUrl}/dashboard/summary`);
  }

  getProfile(): Observable<StudentProfile | null> {
    return this.http.get<StudentProfile | null>(`${this.apiUrl}/profile`);
  }

  saveProfile(profile: StudentProfile): Observable<StudentProfile> {
    return this.http.put<StudentProfile>(`${this.apiUrl}/profile`, profile);
  }

  getOptRecord(): Observable<OptRecord | null> {
    return this.http.get<OptRecord | null>(`${this.apiUrl}/opt-record`);
  }

  saveOptRecord(record: OptRecord): Observable<OptRecord> {
    return this.http.put<OptRecord>(`${this.apiUrl}/opt-record`, record);
  }

  employers(): Observable<Employer[]> {
    return this.http.get<Employer[]>(`${this.apiUrl}/employers`);
  }

  createEmployer(employer: Employer): Observable<Employer> {
    return this.http.post<Employer>(`${this.apiUrl}/employers`, employer);
  }

  updateEmployer(id: number, employer: Employer): Observable<Employer> {
    return this.http.put<Employer>(`${this.apiUrl}/employers/${id}`, employer);
  }

  deleteEmployer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/employers/${id}`);
  }

  deadlines(): Observable<Deadline[]> {
    return this.http.get<Deadline[]>(`${this.apiUrl}/deadlines`);
  }

  generateDeadlines(): Observable<Deadline[]> {
    return this.http.post<Deadline[]>(`${this.apiUrl}/deadlines/generate`, {});
  }

  completeDeadline(id: number): Observable<Deadline> {
    return this.http.put<Deadline>(`${this.apiUrl}/deadlines/${id}/complete`, {});
  }

  reopenDeadline(id: number): Observable<Deadline> {
    return this.http.put<Deadline>(`${this.apiUrl}/deadlines/${id}/pending`, {});
  }

  emailTemplates(): Observable<EmailTemplate[]> {
    return this.http.get<EmailTemplate[]>(`${this.apiUrl}/email-templates`);
  }
}
