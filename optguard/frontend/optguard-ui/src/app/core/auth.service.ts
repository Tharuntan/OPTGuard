import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse, MessageResponse, User } from './models';
import { API_URL } from './runtime-config';

const TOKEN_KEY = 'optguard_token';
const USER_KEY = 'optguard_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = inject(API_URL);
  private readonly userSubject = new BehaviorSubject<User | null>(this.readUser());

  readonly user$ = this.userSubject.asObservable();

  register(payload: { email: string; password: string; fullName: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, payload).pipe(
      tap((response) => this.setSession(response))
    );
  }

  login(payload: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, payload).pipe(
      tap((response) => this.setSession(response))
    );
  }

  updateAccount(payload: { fullName: string }): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/account`, payload).pipe(
      tap((user) => this.setUser(user))
    );
  }

  changePassword(payload: { currentPassword: string; newPassword: string }): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(`${this.apiUrl}/account/password`, payload);
  }

  deleteAccount(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/account`).pipe(
      tap(() => this.logout())
    );
  }

  logout(): void {
    safeLocalStorage()?.removeItem(TOKEN_KEY);
    safeLocalStorage()?.removeItem(USER_KEY);
    this.userSubject.next(null);
  }

  token(): string | null {
    return safeLocalStorage()?.getItem(TOKEN_KEY) ?? null;
  }

  isAuthenticated(): boolean {
    return Boolean(this.token());
  }

  currentUser(): User | null {
    return this.userSubject.value;
  }

  private setSession(response: AuthResponse): void {
    safeLocalStorage()?.setItem(TOKEN_KEY, response.token);
    this.setUser(response.user);
  }

  private setUser(user: User): void {
    safeLocalStorage()?.setItem(USER_KEY, JSON.stringify(user));
    this.userSubject.next(user);
  }

  private readUser(): User | null {
    const stored = safeLocalStorage()?.getItem(USER_KEY);
    if (!stored) {
      return null;
    }
    try {
      return JSON.parse(stored) as User;
    } catch {
      return null;
    }
  }
}

function safeLocalStorage(): Storage | null {
  return typeof localStorage === 'undefined' ? null : localStorage;
}
