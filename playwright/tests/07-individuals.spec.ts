import { test, expect } from '../support/fixtures';
import {
  CreateEntityDialog,
  ProjectView,
} from '../support/selectors';

test.describe('individuals', () => {
  test.beforeEach(async ({ page, project }) => {
    await page.locator(ProjectView.tab('Individuals')).click();
  });

  test('I1: Individuals perspective starts empty for a fresh project', async ({
    page,
  }) => {
    await expect(page.locator(ProjectView.root)).toBeVisible();
    // The list portlet is visible even when empty; just confirm the
    // perspective rendered something interactable.
    await expect(page.locator('text=/Individuals/i').first()).toBeVisible();
  });

  test('I2: create individual Boeing747 (no type)', async ({ page }) => {
    // Individuals tab uses the same Create toolbar pattern.
    await page.locator('button.wp-btn-g--create-individual').first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill('Boeing747');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator('text=Boeing747').first()).toBeVisible({
      timeout: 15_000,
    });
  });

  test('I8: delete an individual', async ({ page }) => {
    await page.locator('button.wp-btn-g--create-individual').first().click();
    await page.locator(CreateEntityDialog.name).fill('TempIndividual');
    await page.locator(CreateEntityDialog.submit).click();

    // Scope the row selector to the list cell — `text=TempIndividual` would
    // also match the project feed link and the frame editor, masking
    // whether the row was actually added/removed.
    const listRow = page
      .locator('.wp-entity-node__display-name')
      .filter({ hasText: 'TempIndividual' });
    await expect(listRow).toBeVisible();

    await listRow.click();
    await page.locator('button.wp-btn-g--delete-individual').first().click();

    // Auto-waiting click on the modal-scoped Delete — `isVisible()` does
    // not auto-wait and was firing before the confirm modal had rendered.
    await page
      .locator('.wp-modal button.wp-btn--dialog:has-text("Delete")')
      .click();

    await expect(listRow).toHaveCount(0);
  });
});
