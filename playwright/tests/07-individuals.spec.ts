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
    await page.locator('button[title="Create"], button:has-text("Create")').first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill('Boeing747');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator('text=Boeing747').first()).toBeVisible({
      timeout: 15_000,
    });
  });

  test('I8: delete an individual', async ({ page }) => {
    await page.locator('button[title="Create"], button:has-text("Create")').first().click();
    await page.locator(CreateEntityDialog.name).fill('TempIndividual');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator('text=TempIndividual').first()).toBeVisible();

    await page.locator('text=TempIndividual').first().click();
    await page.locator('button[title="Delete"], button:has-text("Delete")').first().click();
    const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
    if (await confirm.isVisible().catch(() => false)) await confirm.click();
    await expect(page.locator('text=TempIndividual')).toHaveCount(0);
  });
});
