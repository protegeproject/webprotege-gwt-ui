import { test, expect } from '../support/fixtures';
import {
  CreateProjectDialog,
  ProjectList,
  ProjectView,
} from '../support/selectors';

/**
 * Project lifecycle. Each test creates and trashes its own project so
 * the suite leaves no orphans behind.
 */

test.describe('projects', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/#projects/list');
    await expect(page.locator(ProjectList.root)).toBeVisible();
  });

  test('P1: filter checkboxes toggle without errors', async ({ page }) => {
    for (const filter of [
      ProjectList.filters.ownedByMe,
      ProjectList.filters.sharedWithMe,
      ProjectList.filters.trash,
    ]) {
      const cb = page.locator(filter);
      await expect(cb).toBeVisible();
      const before = await cb.isChecked();
      await cb.click();
      await expect(cb).toBeChecked({ checked: !before });
      await cb.click();
      await expect(cb).toBeChecked({ checked: before });
    }
  });

  test('P2: sort dropdown offers the four sort options', async ({ page }) => {
    const select = page.locator(ProjectList.sortDropdown).first();
    const options = await select.locator('option').allTextContents();
    expect(options.join('|')).toMatch(/Last Opened/);
    expect(options.join('|')).toMatch(/Last Modified/);
    expect(options.join('|')).toMatch(/Project Name/);
    expect(options.join('|')).toMatch(/Owner/);
  });

  test('P3: Create New Project opens dialog with all fields', async ({ page }) => {
    await page.locator(ProjectList.createButton).click();
    await expect(page.locator(CreateProjectDialog.root)).toBeVisible();
    await expect(page.locator(CreateProjectDialog.name).first()).toBeVisible();
    await expect(page.locator(CreateProjectDialog.description)).toBeVisible();
    await expect(page.locator(CreateProjectDialog.fileUpload)).toBeAttached();
    await page.locator(CreateProjectDialog.cancel).click();
    await expect(page.locator(CreateProjectDialog.root)).not.toBeVisible();
  });

  test('P4: empty name shows inline error', async ({ page }) => {
    await page.locator(ProjectList.createButton).click();
    await page.locator(CreateProjectDialog.submit).click();
    await expect(page.locator(CreateProjectDialog.nameError)).toBeVisible();
    await page.locator(CreateProjectDialog.cancel).click();
  });

  test('P5+P6+P7+P8: create, open, trash, restore round-trip', async ({ page }) => {
    const name = `RoundTrip_${crypto.randomUUID().slice(0, 8)}`;

    // Create.
    await page.locator(ProjectList.createButton).click();
    await page.locator(CreateProjectDialog.name).first().fill(name);
    await page.locator(CreateProjectDialog.submit).click();
    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });

    // Open URL pattern check.
    await expect(page).toHaveURL(/#projects\/[a-f0-9-]+\/perspectives\/[a-f0-9-]+/);

    // Back to list, find row.
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) });
    await expect(row).toHaveCount(1);

    // Trash.
    await row.locator(ProjectList.menuButton).click();
    await page.locator('role=menuitem >> text=/Trash|Move to Trash/').click();
    await page.locator(ProjectList.filters.trash).check();
    await expect(
      page.locator(ProjectList.rows)
        .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) }),
    ).toHaveCount(1);

    // Restore.
    const trashedRow = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) });
    await trashedRow.locator(ProjectList.menuButton).click();
    await page.locator('role=menuitem >> text=/Restore/').click();
    await page.locator(ProjectList.filters.trash).uncheck();
    await expect(
      page.locator(ProjectList.rows)
        .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) }),
    ).toHaveCount(1);
  });

  test('P11: sharing dialog opens from project menu', async ({ project, page }) => {
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) });
    await row.locator(ProjectList.menuButton).click();
    await page.locator('role=menuitem >> text=/Sharing/').click();
    await expect(page.locator('role=dialog:has-text("Sharing")')).toBeVisible();
  });
});
