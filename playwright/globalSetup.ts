import { chromium, FullConfig } from '@playwright/test';
import * as fs from 'fs';
import * as path from 'path';
import { ensureTestUser, KEYCLOAK_DEFAULTS, TEST_USER } from './support/keycloak';

/**
 * Runs once before any spec.
 *
 * 1. Creates the fixed E2E test user in the 'webprotege' Keycloak realm if
 *    it does not already exist (idempotent — safe on re-runs).
 * 2. Drives the Keycloak login form once with that user and saves the
 *    resulting cookie/token state to .auth/storageState.json. Every spec
 *    inherits the logged-in state via `use.storageState` in playwright.config.ts.
 *
 * Requires the docker-compose stack to be up. Fails fast with a useful
 * message if Keycloak is unreachable.
 */
async function globalSetup(_config: FullConfig): Promise<void> {
  const baseUrl = process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost';
  const authDir = path.join(__dirname, '.auth');
  const stateFile = path.join(authDir, 'storageState.json');
  if (!fs.existsSync(authDir)) fs.mkdirSync(authDir, { recursive: true });

  await ensureTestUser({ ...KEYCLOAK_DEFAULTS, baseUrl });

  const browser = await chromium.launch();
  const context = await browser.newContext();
  const page = await context.newPage();

  await page.goto(baseUrl + '/');

  // Keycloak's standard form has #username, #password, #kc-login. The
  // webprotege-keycloak custom theme keeps these IDs, so they are stable.
  await page.locator('#username').fill(TEST_USER.username);
  await page.locator('#password').fill(TEST_USER.password);
  await Promise.all([
    page.waitForURL((url) => url.hash.includes('projects/list') || url.pathname.endsWith('/'), {
      timeout: 30_000,
    }),
    page.locator('#kc-login').click(),
  ]);

  await context.storageState({ path: stateFile });
  await browser.close();
}

export default globalSetup;
