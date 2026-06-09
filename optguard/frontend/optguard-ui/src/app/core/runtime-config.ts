import { InjectionToken } from '@angular/core';

export interface RuntimeConfig {
  apiUrl: string;
}

export const LOCAL_API_URL = 'http://localhost:8080/api';
export const PRODUCTION_API_URL = 'https://optguard-api.onrender.com/api';
export const DEFAULT_API_URL = LOCAL_API_URL;

export const API_URL = new InjectionToken<string>('OPTGuard API URL');

export function resolveApiUrl(rawValue: string | null | undefined, hostname = browserHostname()): string {
  const extracted = extractHttpUrl(rawValue);
  if (extracted) {
    return trimTrailingSlash(extracted);
  }
  return isProductionHost(hostname) ? PRODUCTION_API_URL : LOCAL_API_URL;
}

function extractHttpUrl(rawValue: string | null | undefined): string {
  if (!rawValue) {
    return '';
  }
  const match = String(rawValue).match(/https?:\/\/[^\s"'<>]+/);
  if (!match) {
    return '';
  }
  try {
    return new URL(match[0].replace(/[),.;]+$/, '')).toString();
  } catch {
    return '';
  }
}

function isProductionHost(hostname: string): boolean {
  return Boolean(hostname && hostname !== 'localhost' && hostname !== '127.0.0.1');
}

function browserHostname(): string {
  return typeof location === 'undefined' ? '' : location.hostname;
}

function trimTrailingSlash(url: string): string {
  return url.endsWith('/') ? url.slice(0, -1) : url;
}
