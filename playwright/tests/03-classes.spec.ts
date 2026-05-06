import { test, expect } from '../support/fixtures';
import { addPropertyValue } from '../support/frameEditor';
import {
  CreateEntityDialog,
  FrameEditor,
  Hierarchy,
  ProjectView,
} from '../support/selectors';

/**
 * Class hierarchy CRUD. Each test starts with a freshly-created project
 * (via the `project` fixture) sitting on the default Classes perspective.
 */

async function createClassUnder(
  page: import('@playwright/test').Page,
  parentLabel: string,
  newLabel: string,
): Promise<void> {
  await page.locator(Hierarchy.treeNode(parentLabel)).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  await page.locator(CreateEntityDialog.name).fill(newLabel);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(newLabel))).toBeVisible({
    timeout: 15_000,
  });
}

test.describe('class hierarchy', () => {
  test('C1+C2: Classes is default; owl:Thing root with frame editor', async ({
    page,
    project,
  }) => {
    await expect(page.locator(ProjectView.root)).toBeVisible();
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible();
    await page.locator(Hierarchy.treeNode('owl:Thing')).click();
    await expect(page.locator(FrameEditor.annotationsSection)).toBeVisible();
    await expect(page.locator(FrameEditor.classesSection)).toBeVisible();
    await expect(page.locator(FrameEditor.relationshipsSection)).toBeVisible();
  });

  test('C3: create sub-class under owl:Thing', async ({ page, project }) => {
    await createClassUnder(page, 'owl:Thing', 'Vehicle');
  });

  test('C4: empty name is rejected', async ({ page, project }) => {
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.submit).click();
    // Dialog stays open — no class was created.
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.cancel).click();
  });

  test('C5: create child under a non-root class', async ({ page, project }) => {
    await createClassUnder(page, 'owl:Thing', 'Vehicle');
    await createClassUnder(page, 'Vehicle', 'Car');
  });

  test('C6: bulk-create multiple classes from one dialog', async ({ page, project }) => {
    // The Create Classes dialog accepts one name per line in its textarea
    // and creates each as a sibling under the selected parent.
    const names = ['Person', 'Animal', 'ConceptA', 'ConceptB', 'ConceptC'];
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
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

  test('C7: right-click context menu exposes core actions', async ({
    page,
    project,
  }) => {
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click({ button: 'right' });
    const menu = page.locator(Hierarchy.contextMenu);
    await expect(menu).toBeVisible();
    for (const label of ['Create', 'Delete', 'Watch']) {
      await expect(menu.locator(`text=${label}`).first()).toBeVisible();
    }
    await page.keyboard.press('Escape');
  });

  test('C9: drag-and-drop reparents a class', async ({ page, project }) => {
    // graphtree (`edu.stanford.protege.gwt.graphtree`) marks each
    // `.gt-tree__row` as `draggable="true"` and dispatches the standard
    // HTML5 sequence (`dragstart` → `dragover` w/ preventDefault → `drop`).
    // Playwright's `locator.dragTo()` synthesises the full sequence via
    // CDP, which is enough to drive the reparent RPC.
    await createClassUnder(page, 'owl:Thing', 'DragAlpha');
    await createClassUnder(page, 'owl:Thing', 'DragBeta');

    await page
      .locator(Hierarchy.treeNode('DragBeta'))
      .first()
      .dragTo(page.locator(Hierarchy.treeNode('DragAlpha')).first());

    // Reload to verify the move was committed server-side, not just a
    // local DOM tweak. The Classes perspective is the project default.
    await page.reload();
    await expect(page.locator(Hierarchy.treeNode('DragAlpha'))).toBeVisible({
      timeout: 15_000,
    });
    await page
      .locator(Hierarchy.treeNode('DragAlpha'))
      .locator('.gt-tree__handle')
      .click();
    await expect(page.locator(Hierarchy.treeNode('DragBeta'))).toBeVisible();
  });

  test('C10: add multiple annotations with language tags to a class', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'Person');

    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'Human', 'en');
    await addPropertyValue(page, 'Annotations', 'rdfs:label', 'Mensch', 'de');
    await addPropertyValue(page, 'Annotations', 'rdfs:comment', 'An example comment');

    const annotations = page
      .locator(FrameEditor.section('Annotations'))
      .locator(FrameEditor.row);
    // Values are distinct strings so substring matching is unambiguous.
    await expect(annotations.filter({ hasText: 'Human' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'Mensch' })).toHaveCount(1);
    await expect(annotations.filter({ hasText: 'An example comment' })).toHaveCount(1);
    // Spot-check that the language tag survived alongside its literal.
    await expect(annotations.filter({ hasText: 'Human' })).toContainText('en');
    await expect(annotations.filter({ hasText: 'Mensch' })).toContainText('de');
  });

  test('C8: delete a leaf class', async ({ page, project }) => {
    await createClassUnder(page, 'owl:Thing', 'Vehicle');
    await page.locator(Hierarchy.treeNode('Vehicle')).click();
    await page.locator(Hierarchy.toolbar.delete).first().click();
    // Some delete flows show a confirmation prompt; accept if present.
    const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
    if (await confirm.isVisible().catch(() => false)) await confirm.click();
    await expect(page.locator(Hierarchy.treeNode('Vehicle'))).toHaveCount(0);
  });
});
