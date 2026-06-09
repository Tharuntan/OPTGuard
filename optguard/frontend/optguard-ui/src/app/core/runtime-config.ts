import { InjectionToken } from '@angular/core';

export interface RuntimeConfig {
  apiUrl: string;
}

export const DEFAULT_API_URL = 'http://localhost:8080/api';

export const API_URL = new InjectionToken<string>('OPTGuard API URL');
