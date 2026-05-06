import { test, expect, goToPerspective } from '../support/fixtures';
import { addPrimitiveValue, addPropertyValue } from '../support/frameEditor';
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

  test('OP5: bulk-create multiple object properties from one dialog', async ({
    page,
  }) => {
    const names = ['hasPart', 'hasEngine', 'hasWing', 'hasFuselage'];
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill(names.join('\n'));
    await page.locator(CreateEntityDialog.submit).click();
    for (const name of names) {
      await expect(page.locator(Hierarchy.treeNode(name))).toBeVisible({
        timeout: 15_000,
      });
    }
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

  test('OP6: drag-and-drop reparents an object property', async ({ page }) => {
    // graphtree fires standard HTML5 drag/drop events on `.gt-tree__row`.
    // Playwright's `dragTo` drives the dragstart/dragover/drop sequence.
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('opAlpha\nopBeta');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('opAlpha'))).toBeVisible({
      timeout: 15_000,
    });
    await expect(page.locator(Hierarchy.treeNode('opBeta'))).toBeVisible();

    await page
      .locator(Hierarchy.treeNode('opBeta'))
      .first()
      .dragTo(page.locator(Hierarchy.treeNode('opAlpha')).first());

    await page.reload();
    await goToPerspective(page, 'Object Properties');
    // Expansion fires on `mouseup` over the handle; an immediate click
    // post-reload can land before MouseEventMapper is wired up. Retry
    // the click until opBeta surfaces.
    await expect(async () => {
      await page
        .locator(Hierarchy.treeNode('opAlpha'))
        .locator('.gt-tree__handle')
        .click();
      await expect(page.locator(Hierarchy.treeNode('opBeta'))).toBeVisible({
        timeout: 1_500,
      });
    }).toPass({ timeout: 15_000 });
  });

  test('OP7: add multiple annotations with language tags to a property', async ({
    page,
  }) => {
    await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasPart');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible({
      timeout: 15_000,
    });

    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'has part', 'en');
    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'hat Teil', 'de');
    await addPropertyValue(page, 'Annotations', 'rdfs:comment', 'Mereological link');

    const annotations = page
      .locator(FrameEditor.section('Annotations'))
      .locator(FrameEditor.row);
    await expect(annotations.filter({ hasText: 'has part' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'hat Teil' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'Mereological link' })).toHaveCount(1);
    // Language tag is stored in the row's `<input>`, outside textContent.
    await expect(
      annotations.filter({ hasText: 'has part' }).locator('input.gwt-SuggestBox'),
    ).toHaveValue('en');
    await expect(
      annotations.filter({ hasText: 'hat Teil' }).locator('input.gwt-SuggestBox'),
    ).toHaveValue('de');
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
