import { test, expect, goToPerspective } from '../support/fixtures';
import { addPrimitiveValue, addPropertyValue } from '../support/frameEditor';
import {
  CreateEntityDialog,
  FrameEditor,
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

  test('DP4: bulk-create multiple data properties from one dialog', async ({
    page,
  }) => {
    const names = ['hasWeight', 'hasMaxAltitude', 'hasYearBuilt', 'hasName'];
    await page.locator(Hierarchy.treeNode('owl:topDataProperty')).click();
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

  test('DP3: set Domain and Range on a property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topDataProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasWeight');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasWeight'))).toBeVisible({
      timeout: 15_000,
    });

    await addPrimitiveValue(page, 'Domain', 'owl:Thing');
    // `xsd:integer` is one of the built-in datatypes recognised by the
    // PrimitiveDataEditor's suggest box, so no extra entity setup is needed.
    await addPrimitiveValue(page, 'Range', 'xsd:integer');

    const domainRow = page
      .locator(FrameEditor.section('Domain'))
      .locator(FrameEditor.row)
      .filter({ hasText: 'owl:Thing' });
    const rangeRow = page
      .locator(FrameEditor.section('Range'))
      .locator(FrameEditor.row)
      .filter({ hasText: 'xsd:integer' });
    await expect(domainRow).toHaveCount(1);
    await expect(rangeRow).toHaveCount(1);
  });

  test('DP5: drag-and-drop reparents a data property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('owl:topDataProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('dpAlpha\ndpBeta');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('dpAlpha'))).toBeVisible({
      timeout: 15_000,
    });
    await expect(page.locator(Hierarchy.treeNode('dpBeta'))).toBeVisible();

    await page
      .locator(Hierarchy.treeNode('dpBeta'))
      .first()
      .dragTo(page.locator(Hierarchy.treeNode('dpAlpha')).first());
    // Drain the in-flight MoveHierarchyNode RPC before navigating
    // away — see C9 in 03-classes.spec.ts for why.
    await page.waitForLoadState('networkidle');

    await page.reload();
    await goToPerspective(page, 'Data Properties');
    // Expansion fires on `mouseup` over the handle; an immediate click
    // post-reload can land before MouseEventMapper is wired up. Retry
    // the click until dpBeta surfaces.
    await expect(async () => {
      await page
        .locator(Hierarchy.treeNode('dpAlpha'))
        .locator('.gt-tree__handle')
        .click();
      await expect(page.locator(Hierarchy.treeNode('dpBeta'))).toBeVisible({
        timeout: 1_500,
      });
    }).toPass({ timeout: 15_000 });
  });

  test('DP7: add multiple annotations with language tags to a property', async ({
    page,
  }) => {
    await page.locator(Hierarchy.treeNode('owl:topDataProperty')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('hasWeight');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('hasWeight'))).toBeVisible({
      timeout: 15_000,
    });

    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'weight', 'en');
    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'Gewicht', 'de');
    await addPropertyValue(page, 'Annotations', 'rdfs:comment', 'Mass attribute');

    const annotations = page
      .locator(FrameEditor.section('Annotations'))
      .locator(FrameEditor.row);
    // `hasText: 'weight'` would also match the auto-generated
    // `rdfs:label = hasWeight` row, so use a word-boundary regex to
    // pin to the exact literal.
    await expect(annotations.filter({ hasText: /\bweight\b/ })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'Gewicht' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'Mass attribute' })).toHaveCount(1);
    // Language tag is stored in the row's `<input>`, outside textContent.
    await expect(
      annotations.filter({ hasText: /\bweight\b/ }).locator('input.gwt-SuggestBox'),
    ).toHaveValue('en');
    await expect(
      annotations.filter({ hasText: 'Gewicht' }).locator('input.gwt-SuggestBox'),
    ).toHaveValue('de');
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
