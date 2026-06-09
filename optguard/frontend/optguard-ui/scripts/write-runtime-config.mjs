import { mkdirSync, writeFileSync } from 'node:fs';
import { dirname, resolve } from 'node:path';

const apiUrl = process.env.OPTGUARD_API_URL || 'http://localhost:8080/api';
const outputPath = resolve('src/assets/runtime-config.json');

mkdirSync(dirname(outputPath), { recursive: true });
writeFileSync(outputPath, `${JSON.stringify({ apiUrl }, null, 2)}\n`);
