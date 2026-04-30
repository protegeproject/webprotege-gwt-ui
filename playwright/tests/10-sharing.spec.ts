import { test, expect } from '../support/fixtures';
import { Sharing } from '../support/selectors';

/**
 * Sharing settings live on a dedicated SharingSettingsPlace, not a modal
 * dialog. The "Share" button on the project's topbar routes there.
 */

test.describe('sharing', () => {
  test('SH1: sharing page opens from the topbar Share button', async ({
    page,
    project,
  }) => {
    await page.locator(Sharing.topBarButton).click();
    await expect(page).toHaveURL(/#projects\/[a-f0-9-]+\/sharing/);
    await expect(page.locator(Sharing.linkSharingToggle).first()).toBeVisible();
  });

  test('SH2: link sharing toggle reveals permission dropdown', async ({
    page,
    project,
  }) => {
    await page.locator(Sharing.topBarButton).click();
    const toggle = page.locator(Sharing.linkSharingToggle).first();
    await expect(toggle).toBeVisible();
    if (!(await toggle.isChecked())) await toggle.check();
    await expect(page.locator(Sharing.linkSharingPermission).first()).toBeVisible();
  });
});
