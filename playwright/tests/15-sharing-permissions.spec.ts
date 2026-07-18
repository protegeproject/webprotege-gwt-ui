import { Locator, Page } from '@playwright/test';
import { test, expect, projectIdOf } from '../support/fixtures';
import {
  COLLAB_USER,
  KEYCLOAK_DEFAULTS,
  TEST_USER,
  ensureTestUser,
} from '../support/keycloak';
import { Hierarchy, ProjectView, Sharing } from '../support/selectors';

/**
 * Sharing UI, persistence, and permission enforcement.
 *
 * This file used to stop short of clicking Apply because the sharing
 * write-path was known-broken on this deployment (see GH #289). Several
 * backend fixes have since landed that this suite now guards against
 * regressing: an owner could lose their own access on save, a broken user
 * lookup could make every save fail outright with a spurious "duplicate
 * user" error, and sharing with someone who has never registered used to
 * just silently vanish instead of staying visible (that person is now
 * emailed and gains access automatically once they register and verify).
 * SH2 and SH3 assert the first and last of those directly.
 *
 * SH2 in particular is what it is — a fresh login actually opening the
 * project, not just a row appearing in the sharing settings — because that
 * exact assertion is what caught two independent, previously-hidden
 * NullPointerExceptions in webprotege-authorization-service that made
 * every capability check fail for an ordinary collaborator (a role-
 * hierarchy-cycle logging crash, and a JWT-roles-extraction crash for
 * anyone with no "webprotege" client roles - which is nearly everyone
 * except admins). Checking only the sharing-settings row would have
 * missed both. The `project` fixture also fails any test outright if a
 * backend error surfaces during it, so a regression in the "duplicate
 * user" style is caught even if no assertion here happens to target it.
 */

test.beforeAll(async () => {
  // A plain realm user with no webprotege client roles and no share on any
  // project — used to prove the access gate denies non-collaborators, and
  // reused as the person shared with in SH2 once granted.
  await ensureTestUser(KEYCLOAK_DEFAULTS, COLLAB_USER, []);
});

async function loginAs(page: Page, user = COLLAB_USER): Promise<void> {
  await page.goto('/');
  await page.locator('#username').fill(user.username);
  await page.locator('#password').fill(user.password);
  await page.locator('#kc-login').click();
  await page.waitForURL(/#projects\/list/, { timeout: 30_000 });
}

/** Clicks Apply and waits for the save round-trip to complete before returning. */
async function applyAndAwaitSave(page: Page): Promise<void> {
  await Promise.all([
    page.waitForResponse((response) => /dispatchservice/i.test(response.url())),
    page.locator(Sharing.applyButton).click(),
  ]);
}

/**
 * Finds the sharing-page row whose person field currently holds `value`.
 * Polls rather than a single pass: the rows re-render asynchronously as
 * the page's data loads. Throws if `value` never shows up within
 * `timeout` — the failure mode a pre-#177 regression would hit, since an
 * unresolved entry used to be silently dropped rather than kept.
 */
async function findPersonRowByValue(
  page: Page,
  value: string,
  timeout = 15_000,
): Promise<Locator> {
  const deadline = Date.now() + timeout;
  for (;;) {
    const rows = page.locator(Sharing.personRow);
    const count = await rows.count();
    for (let i = 0; i < count; i++) {
      const row = rows.nth(i);
      const input = row.locator(Sharing.personInput).first();
      if ((await input.count()) > 0 && (await input.inputValue()) === value) {
        return row;
      }
    }
    if (Date.now() > deadline) {
      throw new Error(
        `No sharing row found with person value "${value}" after ${timeout}ms`,
      );
    }
    await page.waitForTimeout(300);
  }
}

/** Types `value` into the sharing page's trailing blank row and commits it. */
async function addPersonRow(
  page: Page,
  value: string,
  permission: 'VIEW' | 'COMMENT' | 'EDIT' | 'MANAGE',
): Promise<void> {
  const rowsBefore = await page.locator(Sharing.personRow).count();
  const blankRow = page.locator(Sharing.personRow).last();
  const input = blankRow.locator(Sharing.personInput).first();
  await input.click();
  // The oracle fires per keyup, so pressSequentially (not fill); Escape
  // dismisses the completion popup and Tab blurs to commit the row value.
  await input.pressSequentially(value, { delay: 30 });
  await page.keyboard.press('Escape');
  await page.keyboard.press('Tab');
  // The value-list runs in AUTOMATIC mode, so committing the row appends a
  // fresh trailing blank — the list grows by one.
  await expect(page.locator(Sharing.personRow)).toHaveCount(rowsBefore + 1, {
    timeout: 15_000,
  });
  const committedRow = page.locator(Sharing.personRow).nth(rowsBefore - 1);
  await committedRow.locator(Sharing.permissionSelect).selectOption(permission);
}

test.describe('sharing and permissions', () => {
  test('SH1: a user without access is denied the project (Forbidden)', async ({
    page,
    project,
    browser,
  }) => {
    // Fresh context: Keycloak SSO cookies are per-context, so this logs in
    // cleanly as the collaborator without touching the owner's session.
    const ctx = await browser.newContext({
      storageState: { cookies: [], origins: [] },
    });
    try {
      const p2 = await ctx.newPage();
      await loginAs(p2, COLLAB_USER);
      // Positive control: a valid user reaches their own (empty) project list.
      await expect(p2).toHaveURL(/#projects\/list/);

      // The collaborator has no access to the owner's project, so opening
      // it directly must be refused rather than rendering the editor.
      await p2.goto(project.url);
      await expect(p2.getByText('Forbidden').first()).toBeVisible({
        timeout: 30_000,
      });
      await expect(p2.locator(ProjectView.root)).toHaveCount(0);
      await expect(p2.locator(Hierarchy.treeNode('owl:Thing'))).toHaveCount(0);
    } finally {
      await ctx.close();
    }
  });

  test('SH2: sharing with a registered user persists, grants access, and the owner keeps theirs', async ({
    page,
    project,
    browser,
  }) => {
    test.setTimeout(90_000);
    const projectId = projectIdOf(project);

    await page.locator(Sharing.topBarButton).click();
    await expect(page).toHaveURL(new RegExp(`#projects/${projectId}/sharing`));
    await expect(page.locator(Sharing.personRow).last()).toBeVisible({
      timeout: 15_000,
    });

    await addPersonRow(page, COLLAB_USER.username, 'EDIT');
    // The SuggestBox keeps the typed text as its live value (GWT sets the
    // value property, not the attribute, so assert with toHaveValue).
    await expect(
      (await findPersonRowByValue(page, COLLAB_USER.username)).locator(
        Sharing.personInput,
      ),
    ).toHaveValue(COLLAB_USER.username);

    await applyAndAwaitSave(page);

    // Verify persistence from a brand-new session rather than reloading
    // this tab: a hard reload round-trips through Keycloak's silent SSO
    // renewal and can drop the deep-link hash, landing back on the
    // project list for reasons unrelated to sharing at all. A fresh login
    // has no client-side state to fall back on, so it can only be
    // rendering what the backend actually has stored. This also proves
    // the owner still has their own access after the save (the scenario
    // GH backend-service#176 fixed) — a locked-out owner would fail the
    // very first assertion below.
    const ownerCtx = await browser.newContext({
      storageState: { cookies: [], origins: [] },
    });
    try {
      const ownerPage = await ownerCtx.newPage();
      await loginAs(ownerPage, TEST_USER);
      await ownerPage.goto(project.url);
      await expect(ownerPage.locator(ProjectView.root)).toBeVisible({
        timeout: 30_000,
      });
      await ownerPage.locator(Sharing.topBarButton).click();
      await expect(ownerPage.locator(Sharing.personRow).last()).toBeVisible({
        timeout: 15_000,
      });
      const persistedRow = await findPersonRowByValue(
        ownerPage,
        COLLAB_USER.username,
      );
      await expect(persistedRow.locator(Sharing.permissionSelect)).toHaveValue(
        'EDIT',
      );
    } finally {
      await ownerCtx.close();
    }

    // The collaborator now actually has access — proves the grant landed
    // on the identity that logs in, not just that a row renders.
    const collabCtx = await browser.newContext({
      storageState: { cookies: [], origins: [] },
    });
    try {
      const p2 = await collabCtx.newPage();
      await loginAs(p2, COLLAB_USER);
      await p2.goto(project.url);
      await expect(p2.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
      await expect(p2.getByText('Forbidden')).toHaveCount(0);
    } finally {
      await collabCtx.close();
    }
  });

  test('SH3: sharing with an unregistered email keeps the row visible afterwards', async ({
    page,
    project,
    browser,
  }) => {
    const projectId = projectIdOf(project);
    // A never-registered address in the reserved .invalid TLD (RFC 2606) —
    // guaranteed not to exist, and never deliverable, so nothing outside
    // this test can ever be affected by it.
    const pendingEmail = `pending-${crypto.randomUUID().slice(0, 8)}@e2e.invalid`;

    await page.locator(Sharing.topBarButton).click();
    await expect(page).toHaveURL(new RegExp(`#projects/${projectId}/sharing`));
    await expect(page.locator(Sharing.personRow).last()).toBeVisible({
      timeout: 15_000,
    });

    await addPersonRow(page, pendingEmail, 'VIEW');
    await applyAndAwaitSave(page);

    // Fresh session, not the tab that made the save — proves the entry
    // was actually persisted server-side rather than just reflecting
    // local UI state that was never round-tripped. Before the
    // pending-invitation fix, an unresolved entry was dropped silently on
    // save and would simply not be here.
    const ctx = await browser.newContext({
      storageState: { cookies: [], origins: [] },
    });
    try {
      const freshPage = await ctx.newPage();
      await loginAs(freshPage, TEST_USER);
      await freshPage.goto(project.url);
      await expect(freshPage.locator(ProjectView.root)).toBeVisible({
        timeout: 30_000,
      });
      await freshPage.locator(Sharing.topBarButton).click();
      await expect(freshPage.locator(Sharing.personRow).last()).toBeVisible({
        timeout: 15_000,
      });
      const pendingRow = await findPersonRowByValue(freshPage, pendingEmail);
      await expect(pendingRow.locator(Sharing.permissionSelect)).toHaveValue(
        'VIEW',
      );
    } finally {
      await ctx.close();
    }
  });
});
