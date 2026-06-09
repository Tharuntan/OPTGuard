import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { API_URL, DEFAULT_API_URL, RuntimeConfig } from './app/core/runtime-config';

async function loadRuntimeConfig(): Promise<RuntimeConfig> {
  try {
    const response = await fetch('/assets/runtime-config.json', { cache: 'no-store' });
    if (!response.ok) {
      return { apiUrl: DEFAULT_API_URL };
    }
    return await response.json() as RuntimeConfig;
  } catch {
    return { apiUrl: DEFAULT_API_URL };
  }
}

loadRuntimeConfig()
  .then((runtimeConfig) => bootstrapApplication(AppComponent, {
    ...appConfig,
    providers: [
      ...(appConfig.providers ?? []),
      { provide: API_URL, useValue: runtimeConfig.apiUrl || DEFAULT_API_URL }
    ]
  }))
  .catch((err) => console.error(err));
