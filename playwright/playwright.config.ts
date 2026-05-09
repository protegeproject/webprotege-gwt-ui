import { defineConfig, devices } from '@playwright/test';
import * as path from 'path';

const BASE_URL = process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost';
const STORAGE_STATE = path.join(__dirname, '.auth', 'storageState.json');

export default defineConfig({
  testDir: './tests',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: 1,
  workers: 4,
  reporter: process.env.CI
    ? [['github'], ['html', { open: 'never' }]]
    : [['list'], ['html', { open: 'never' }]],
  globalSetup: require.resolve('./globalSetup'),
  globalTeardown: require.resolve('./globalTeardown'),
  timeout: 60_000,
  expect: { timeout: 10_000 },
  use: {
    baseURL: BASE_URL,
    storageState: STORAGE_STATE,
    trace: 'on-first-retry',
    screenshot: 'off',
    video: 'retain-on-failure',
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
