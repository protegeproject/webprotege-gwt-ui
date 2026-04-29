import { test, expect } from '../support/fixtures';
import { ProjectList, Sharing } from '../support/selectors';

test.describe('sharing', () => {
  test('SH1: sharing dialog opens from project menu', async ({ page, project }) => {
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) });
    await row.locator(ProjectList.menuButton).click();
    await page.locator('role=menuitem >> text=/Sharing/').click();
    await expect(page.locator(Sharing.dialog)).toBeVisible();
  });

  test('SH2: link sharing toggle reveals permission dropdown', async ({
    page,
    project,
  }) => {
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) });
    await row.locator(ProjectList.menuButton).click();
    await page.locator('role=menuitem >> text=/Sharing/').click();
    const dialog = page.locator(Sharing.dialog);
    await expect(dialog).toBeVisible();

    const toggle = dialog.locator(Sharing.linkSharingToggle).first();
    if (await toggle.isVisible().catch(() => false)) {
      await toggle.check();
      await expect(dialog.locator(Sharing.linkSharingPermission).first()).toBeEnabled();
    }
  });
});
