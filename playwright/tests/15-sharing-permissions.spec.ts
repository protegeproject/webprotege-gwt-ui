import { Page } from '@playwright/test';
import { test, expect, projectIdOf } from '../support/fixtures';
import {
  COLLAB_USER,
  KEYCLOAK_DEFAULTS,
  ensureTestUser,
} from '../support/keycloak';
import { Hierarchy, ProjectView, Sharing } from '../support/selectors';

/**
 * Sharing UI and permission enforcement.
 *
 * SCOPE NOTE: this deployment's sharing WRITE path is broken — after a
 * SetProjectSharingSettings apply, the sharing page reloads with an empty
 * people list (the stored settings no longer render), so a shared user
 * never actually gains access. Persistence of a share and VIEW-only
 * capability gating therefore cannot be asserted green here. What is
 * reliable and covered below: the add-person editor UI, and the
 * authorization gate that denies a user with no access (which needs no
 * write). The sharing page opening + link-sharing toggle are covered by
 * 10-sharing.spec.ts.
 */

test.beforeAll(async () => {
  // A plain realm user with no webprotege client roles and no share on any
  // project — used to prove the access gate denies non-collaborators.
  await ensureTestUser(KEYCLOAK_DEFAULTS, COLLAB_USER, []);
});

async function loginAs(page: Page, user = COLLAB_USER): Promise<void> {
  await page.goto('/');
  await page.locator('#username').fill(user.username);
  await page.locator('#password').fill(user.password);
  await page.locator('#kc-login').click();
  await page.waitForURL(/#projects\/list/, { timeout: 30_000 });
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

  test('SH2: the sharing page accepts a new person row with a permission', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);

    await page.locator(Sharing.topBarButton).click();
    await expect(page).toHaveURL(new RegExp(`#projects/${projectId}/sharing`));
    await expect(page.locator(Sharing.personRow).last()).toBeVisible({
      timeout: 15_000,
    });
    const rowsBefore = await page.locator(Sharing.personRow).count();

    // Type a username into the trailing blank row's SuggestBox. The oracle
    // fires per keyup, so pressSequentially (not fill); Escape dismisses
    // the completion popup and Tab blurs to commit the row value.
    const blankRow = page.locator(Sharing.personRow).last();
    const input = blankRow.locator(Sharing.personInput).first();
    await input.click();
    await input.pressSequentially(COLLAB_USER.username, { delay: 30 });
    await page.keyboard.press('Escape');
    await page.keyboard.press('Tab');

    // The value-list runs in AUTOMATIC mode, so committing the row appends
    // a fresh trailing blank — the list grows by one, and our row is the
    // one just before the new blank.
    await expect(page.locator(Sharing.personRow)).toHaveCount(rowsBefore + 1, {
      timeout: 15_000,
    });
    const committedRow = page.locator(Sharing.personRow).nth(rowsBefore - 1);
    // The SuggestBox keeps the typed text as its live value (GWT sets the
    // value property, not the attribute, so assert with toHaveValue).
    await expect(committedRow.locator(Sharing.personInput)).toHaveValue(
      COLLAB_USER.username,
    );
    // The row exposes the permission selector (values are the enum names).
    await committedRow.locator(Sharing.permissionSelect).selectOption('EDIT');
    await expect(committedRow.locator(Sharing.permissionSelect)).toHaveValue(
      'EDIT',
    );

    // NOTE: not applied — the SetProjectSharingSettings write-path is
    // broken on this deployment (see the file header), so persistence is
    // deliberately not asserted.
  });
});
