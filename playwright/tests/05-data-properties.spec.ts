import { test, expect, goToPerspective } from '../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
} from '../support/selectors';

test.describe('data properties', () => {
  test.beforeEach(async ({ page, project }) => {
    await goToPerspective(page, 'Data Properties');
    await expect(page.locator(Hierarchy.treeNode('owl:topDataProperty'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('DP1: Data Properties perspective shows the topDataProperty root', async ({
    page,
  }) => {
    await expect(page.locator(Hierarchy.treeNode('owl:topDataProperty'))).toBeVisible();
  });

  test('DP2: create data property hasWeight', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topDataProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasWeight');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasWeight'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('DP6: delete a data property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topDataProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('tempDataProp');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('tempDataProp'))).toBeVisible();

    await page.locator(Hierarchy.treeNode('tempDataProp')).click();
    await page.locator(Hierarchy.toolbar.delete).first().click();
    const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
    if (await confirm.isVisible().catch(() => false)) await confirm.click();
    await expect(page.locator(Hierarchy.treeNode('tempDataProp'))).toHaveCount(0);
  });
});
