import { test, expect, goToPerspective } from '../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
} from '../support/selectors';

test.describe('annotation properties', () => {
  test.beforeEach(async ({ page, project }) => {
    await goToPerspective(page, 'Annotation Properties');
    // Standard built-in annotation properties are guaranteed to be present.
    await expect(page.locator(Hierarchy.treeNode('rdfs:label'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('AP1: Annotation Properties perspective lists rdfs:label', async ({ page }) => {
    await expect(page.locator(Hierarchy.treeNode('rdfs:label'))).toBeVisible();
    await expect(page.locator(Hierarchy.treeNode('rdfs:comment'))).toBeVisible();
  });

  test('AP2: create custom annotation property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('rdfs:label')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('icaoCode');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('icaoCode'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('AP4: delete a custom annotation property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('rdfs:label')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('tempAnnotation');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('tempAnnotation'))).toBeVisible();

    await page.locator(Hierarchy.treeNode('tempAnnotation')).click();
    await page.locator(Hierarchy.toolbar.delete).first().click();
    const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
    if (await confirm.isVisible().catch(() => false)) await confirm.click();
    await expect(page.locator(Hierarchy.treeNode('tempAnnotation'))).toHaveCount(0);
  });
});
