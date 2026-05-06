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

  test('P4: empty name shows alert', async ({ page }) => {
    // The Create Project view validates the name via a MessageBox alert
    // ("Project name missing" / "Please enter a project name"), not an
    // inline form error. The alert overlays the create-project modal, so
    // the surrounding modal's Cancel button is non-clickable until the
    // alert is dismissed.
    await page.locator(ProjectList.createButton).click();
    await page.locator(CreateProjectDialog.submit).click();
    await expect(page.locator('text=Please enter a project name')).toBeVisible();
    await page.locator('.wp-modal button:has-text("OK")').first().click();
    await page.locator(CreateProjectDialog.cancel).click();
  });

  test('P5+P6+P7+P8: create, open, trash, restore round-trip', async ({ page }) => {
    const name = `RoundTrip_${crypto.randomUUID().slice(0, 8)}`;

    // Create. Submitting the dialog leaves the user on the project list —
    // there is no automatic navigation. Wait for the new row, then click its
    // name cell to open the project.
    await page.locator(ProjectList.createButton).click();
    await page.locator(CreateProjectDialog.name).first().fill(name);
    await page.locator(CreateProjectDialog.submit).click();
    const newRow = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) });
    await expect(newRow).toBeVisible({ timeout: 30_000 });
    await newRow.locator(ProjectList.nameCell).click();
    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });

    // Open URL pattern check.
    await expect(page).toHaveURL(/#projects\/[a-f0-9-]+\/perspectives\/[a-f0-9-]+/);

    // Back to list, find row.
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) });
    await expect(row).toHaveCount(1);

    // Trash. The popup menu (AvailableProjectPresenter#addTrashAction)
    // tracks selection via mousemove and executes on mouseup, so hover the
    // item before clicking to set selectedIndex.
    // TrashManagerRequestHandlerImpl now fires ProjectMovedToTrashEvent
    // on dispatch success (matching how CreateNewProjectPresenter fires
    // ProjectCreatedEvent), so ProjectManagerPresenter updates the cache
    // and the row drops out of the default view without a reload — same
    // as a real user would see.
    await row.locator(ProjectList.menuButton).click();
    const trashItem = page.locator('.wp-popup-menu__item').filter({ hasText: /Move to trash/i });
    await trashItem.hover();
    await trashItem.click();
    // Default filters are OWNED_BY_ME + SHARED_WITH_ME (no Trash); a trashed
    // project must NOT appear there once the cache has been updated.
    await expect(row).toHaveCount(0);
    await page.locator(ProjectList.filters.trash).check();
    await expect(row).toHaveCount(1);

    // Restore. The same menu item flips to "Remove from trash" when the
    // project is already trashed.
    await row.locator(ProjectList.menuButton).click();
    const restoreItem = page.locator('.wp-popup-menu__item').filter({ hasText: /Remove from trash/i });
    await expect(restoreItem).toBeVisible();
    await restoreItem.hover();
    await restoreItem.click();
    // ProjectMovedFromTrashEvent fires on dispatch success → the cache
    // entry flips back to inTrash=false. With OWNED_BY_ME + SHARED_WITH_ME
    // + TRASH all checked the row stays visible (now matching
    // OWNED_BY_ME instead of TRASH — filters are OR'd), so to prove the
    // restore actually took effect, drop the Trash filter and confirm the
    // row is still listed under the defaults.
    await page.locator(ProjectList.filters.trash).uncheck();
    await expect(row).toHaveCount(1);
  });

  test('P11: project row menu exposes core actions', async ({ project, page }) => {
    // The project list row menu (AvailableProjectPresenter) exposes Open,
    // Open in new window, Download and Move to trash. Sharing lives on the
    // project's topbar, not in this menu.
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) });
    await row.locator(ProjectList.menuButton).click();
    // Use exact-text match — "Open" alone would also match "Open in new window".
    for (const label of [/^Open$/, /^Download$/, /^Move to trash$/]) {
      await expect(page.locator('.wp-popup-menu__item').filter({ hasText: label })).toBeVisible();
    }
  });
});
