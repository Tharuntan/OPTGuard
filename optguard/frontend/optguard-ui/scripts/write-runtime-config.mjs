import { mkdirSync, writeFileSync } from 'node:fs';
import { dirname, resolve } from 'node:path';

const LOCAL_API_URL = 'http://localhost:8080/api';
const PRODUCTION_API_URL = 'https://optguard-api.onrender.com/api';

const apiUrl = resolveApiUrl(process.env.OPTGUARD_API_URL, Boolean(process.env.VERCEL));
const outputPath = resolve('src/assets/runtime-config.json');

mkdirSync(dirname(outputPath), { recursive: true });
writeFileSync(outputPath, `${JSON.stringify({ apiUrl }, null, 2)}\n`);

function resolveApiUrl(rawValue, productionBuild) {
  const extracted = extractHttpUrl(rawValue);
  if (extracted) {
    return trimTrailingSlash(extracted);
  }
  return productionBuild ? PRODUCTION_API_URL : LOCAL_API_URL;
}

function extractHttpUrl(rawValue) {
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

function trimTrailingSlash(url) {
  return url.endsWith('/') ? url.slice(0, -1) : url;
}
