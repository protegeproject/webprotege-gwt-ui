import { test, expect, goToPerspective } from '../support/fixtures';
import { addPrimitiveValue } from '../support/frameEditor';
import {
  CreateEntityDialog,
  FrameEditor,
  Hierarchy,
} from '../support/selectors';

test.describe('object properties', () => {
  test.beforeEach(async ({ page, project }) => {
    await goToPerspective(page, 'Object Properties');
    await expect(page.locator(Hierarchy.treeNode('owl:topObjectProperty'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('OP1: switch to Object Properties shows the topObjectProperty root', async ({
    page,
  }) => {
    await expect(page.locator(Hierarchy.treeNode('owl:topObjectProperty'))).toBeVisible();
  });

  test('OP2: create object property hasPart', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill('hasPart');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('OP3: create sub-property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasPart');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible();

    await page.locator(Hierarchy.treeNode('hasPart')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasEngine');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasEngine'))).toBeVisible({
      timeout: 15_000,
    });
  });

  test('OP4: set Domain and Range on a property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasPart');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible({
      timeout: 15_000,
    });

    await addPrimitiveValue(page, 'Domain', 'owl:Thing');
    await addPrimitiveValue(page, 'Range', 'owl:Thing');

    const domainRow = page
      .locator(FrameEditor.section('Domain'))
      .locator(FrameEditor.row)
      .filter({ hasText: 'owl:Thing' });
    const rangeRow = page
      .locator(FrameEditor.section('Range'))
      .locator(FrameEditor.row)
      .filter({ hasText: 'owl:Thing' });
    await expect(domainRow).toHaveCount(1);
    await expect(rangeRow).toHaveCount(1);
  });

  test('OP8: delete a property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('temporaryProp');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('temporaryProp'))).toBeVisible();

    await page.locator(Hierarchy.treeNode('temporaryProp')).click();
    await page.locator(Hierarchy.toolbar.delete).first().click();
    const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
    if (await confirm.isVisible().catch(() => false)) await confirm.click();
    await expect(page.locator(Hierarchy.treeNode('temporaryProp'))).toHaveCount(0);
  });
});
