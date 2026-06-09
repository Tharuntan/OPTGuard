import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { API_URL, RuntimeConfig, resolveApiUrl } from './app/core/runtime-config';

async function loadRuntimeConfig(): Promise<RuntimeConfig> {
  try {
    const response = await fetch('/assets/runtime-config.json', { cache: 'no-store' });
    if (!response.ok) {
      return { apiUrl: '' };
    }
    return await response.json() as RuntimeConfig;
  } catch {
    return { apiUrl: '' };
  }
}

loadRuntimeConfig()
  .then((runtimeConfig) => bootstrapApplication(AppComponent, {
    ...appConfig,
    providers: [
      ...(appConfig.providers ?? []),
      { provide: API_URL, useValue: resolveApiUrl(runtimeConfig.apiUrl) }
    ]
  }))
  .catch((err) => console.error(err));
