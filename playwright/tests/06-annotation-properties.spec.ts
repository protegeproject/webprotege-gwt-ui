import { test, expect, goToPerspective } from '../support/fixtures';
import { addPropertyValue } from '../support/frameEditor';
import {
  CreateEntityDialog,
  FrameEditor,
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

  test('AP3: bulk-create multiple annotation properties from one dialog', async ({
    page,
  }) => {
    const names = ['icaoCode', 'iataCode', 'manufacturerNote'];
    await page.locator(Hierarchy.treeNode('rdfs:label')).click();
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

  test('AP5: drag-and-drop reparents an annotation property', async ({ page }) => {
    await page.locator(Hierarchy.treeNode('rdfs:label')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('apAlpha\napBeta');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('apAlpha'))).toBeVisible({
      timeout: 15_000,
    });
    await expect(page.locator(Hierarchy.treeNode('apBeta'))).toBeVisible();

    await page
      .locator(Hierarchy.treeNode('apBeta'))
      .first()
      .dragTo(page.locator(Hierarchy.treeNode('apAlpha')).first());
    // Drain the in-flight MoveHierarchyNode RPC before navigating
    // away — see C9 in 03-classes.spec.ts for why.
    await page.waitForLoadState('networkidle');

    await page.reload();
    await goToPerspective(page, 'Annotation Properties');
    // Expansion fires on `mouseup` over the handle; an immediate click
    // post-reload can land before MouseEventMapper is wired up. Retry
    // the click until apBeta surfaces.
    await expect(async () => {
      await page
        .locator(Hierarchy.treeNode('apAlpha'))
        .locator('.gt-tree__handle')
        .click();
      await expect(page.locator(Hierarchy.treeNode('apBeta'))).toBeVisible({
        timeout: 1_500,
      });
    }).toPass({ timeout: 15_000 });
  });

  test('AP6: add multiple annotations with language tags to a property', async ({
    page,
  }) => {
    await page.locator(Hierarchy.treeNode('rdfs:label')).click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('icaoCode');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('icaoCode'))).toBeVisible({
      timeout: 15_000,
    });

    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'ICAO code', 'en');
    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'Code OACI', 'fr');
    await addPropertyValue(page, 'Annotations', 'rdfs:comment', 'Four-letter airport identifier');

    const annotations = page
      .locator(FrameEditor.section('Annotations'))
      .locator(FrameEditor.row);
    await expect(annotations.filter({ hasText: 'ICAO code' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'Code OACI' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'Four-letter airport identifier' })).toHaveCount(1);
    // Language tag is stored in the row's `<input>`, outside textContent.
    await expect(
      annotations.filter({ hasText: 'ICAO code' }).locator('input.gwt-SuggestBox'),
    ).toHaveValue('en');
    await expect(
      annotations.filter({ hasText: 'Code OACI' }).locator('input.gwt-SuggestBox'),
    ).toHaveValue('fr');
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
