import { defineConfig, devices } from '@playwright/test';
import * as path from 'path';

const BASE_URL = process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost';
const STORAGE_STATE = path.join(__dirname, '.auth', 'storageState.json');

/**
 * Performance measurement runs. Kept apart from playwright.config.ts on
 * purpose: retries would silently double-run a measurement and parallel
 * workers would contaminate timings with CPU contention on the shared
 * docker stack, so this config pins workers to 1 and retries to 0.
 * Invoke with `npm run test:perf`.
 */
export default defineConfig({
  testDir: './perf',
  testMatch: /.*\.perf\.ts/,
  fullyParallel: false,
  retries: 0,
  workers: 1,
  reporter: [['list'], ['html', { open: 'never' }]],
  globalSetup: require.resolve('./globalSetup'),
  globalTeardown: require.resolve('./globalTeardown'),
  // Seeding plus the parked polling windows push individual tests well
  // past the normal 60s budget.
  timeout: 420_000,
  expect: { timeout: 10_000 },
  use: {
    baseURL: BASE_URL,
    storageState: STORAGE_STATE,
    trace: 'off',
    screenshot: 'off',
    video: 'off',
    actionTimeout: 15_000,
    navigationTimeout: 30_000,
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});
