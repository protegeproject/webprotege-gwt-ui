import { test, expect } from '../support/fixtures';
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
