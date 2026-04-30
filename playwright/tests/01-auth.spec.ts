import { test, expect } from '@playwright/test';
import { Login, ProjectList, TopBar } from '../support/selectors';
import { TEST_USER } from '../support/keycloak';

/**
 * Auth specs run unauthenticated — they explicitly opt out of the shared
 * storageState by setting `storageState: undefined` on the test, so each
 * test sees the Keycloak login redirect from a clean session.
 */

test.use({ storageState: { cookies: [], origins: [] } });

test.describe('authentication', () => {
  test('A1: anonymous access redirects to Keycloak login', async ({ page }) => {
    await page.goto('/');
    await expect(page).toHaveURL(/\/keycloak\/realms\/webprotege\/protocol\/openid-connect\/auth/);
    await expect(page.locator(Login.username)).toBeVisible();
    await expect(page.locator(Login.password)).toBeVisible();
    await expect(page.locator(Login.submit)).toBeVisible();
  });

  test('A2: valid credentials land on projects list', async ({ page }) => {
    await page.goto('/');
    await page.locator(Login.username).fill(TEST_USER.username);
    await page.locator(Login.password).fill(TEST_USER.password);
    await page.locator(Login.submit).click();
    await expect(page).toHaveURL(/#projects\/list/, { timeout: 30_000 });
    await expect(page.locator(ProjectList.root)).toBeVisible();
  });

  test('A3: wrong password shows error and stays on login', async ({ page }) => {
    await page.goto('/');
    await page.locator(Login.username).fill(TEST_USER.username);
    await page.locator(Login.password).fill('definitely-not-the-password');
    await page.locator(Login.submit).click();
    // Stay on Keycloak's auth endpoint after a failed POST — Keycloak
    // re-renders the form with an inline error.
    await expect(page).toHaveURL(/\/keycloak\/realms\/webprotege\//);
    await expect(page.locator(Login.error)).toBeVisible();
  });

  test('A4: sign out returns to login', async ({ page }) => {
    // First log in like A2.
    await page.goto('/');
    await page.locator(Login.username).fill(TEST_USER.username);
    await page.locator(Login.password).fill(TEST_USER.password);
    await page.locator(Login.submit).click();
    await expect(page).toHaveURL(/#projects\/list/);

    // The user-menu container reveals a "Sign Out" item on click. The
    // selector needs the container to be present first, then we open it.
    const userMenu = page.locator(TopBar.userMenu);
    await expect(userMenu).toBeVisible();
    await userMenu.click();
    await page.locator(TopBar.signOut).click();

    await expect(page).toHaveURL(
      /\/keycloak\/realms\/webprotege\/protocol\/openid-connect\/auth|\/$/,
      { timeout: 30_000 },
    );
  });

  test('A5: reload after sign-out preserves logged-out state', async ({ page, context }) => {
    // Log in, then sign out (reuse A4 path).
    await page.goto('/');
    await page.locator(Login.username).fill(TEST_USER.username);
    await page.locator(Login.password).fill(TEST_USER.password);
    await page.locator(Login.submit).click();
    await expect(page).toHaveURL(/#projects\/list/);

    await page.locator(TopBar.userMenu).click();
    await page.locator(TopBar.signOut).click();

    // Clear nothing manually — just navigate again. Should redirect to login.
    await page.goto('/');
    await expect(page).toHaveURL(/\/keycloak\/realms\/webprotege\//);
    await expect(page.locator(Login.submit)).toBeVisible();
  });
});
