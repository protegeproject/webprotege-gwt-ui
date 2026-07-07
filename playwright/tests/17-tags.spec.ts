import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import { projectIdOf } from '../support/fixtures';
import {
  CreateEntityDialog,
  EntityTags,
  Hierarchy,
  Modal,
  ProjectMenu,
  SettingsPage,
  TagsPage,
} from '../support/selectors';

/**
 * Project tags and per-entity tag assignment. The tags page is a
 * ValueListFlexEditor in AUTOMATIC mode — a trailing blank row is always
 * present, and a fresh row gets a random color, so filling just the
 * Label yields a well-formed tag. Tag inputs are plain
 * PlaceholderTextBoxes: fill() + Tab is enough (no SuggestBox
 * choreography).
 */

async function createProjectTag(
  page: Page,
  projectId: string,
  label: string,
): Promise<void> {
  await page.goto(`/#projects/${projectId}/tags`);
  await expect(page.locator(SettingsPage.section('Tags'))).toBeVisible({
    timeout: 30_000,
  });
  const blankRow = page.locator(TagsPage.row).last();
  await blankRow.locator(TagsPage.labelInput).fill(label);
  await page.keyboard.press('Tab');
  await page.locator(SettingsPage.apply).click();
  await page.waitForLoadState('networkidle');
}

test.describe('tags', () => {
  test('TG1: Tags page opens from the Project menu', async ({ page, project }) => {
    await page.locator(ProjectMenu.button).click();
    const tagsItem = page.locator(ProjectMenu.item('Tags'));
    await tagsItem.hover();
    await tagsItem.click();

    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/tags/);
    await expect(page.locator(SettingsPage.section('Tags'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(
      page.locator(SettingsPage.section('Tag Assignments')),
    ).toBeVisible();
  });

  test('TG2: creating a tag persists across a reload', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await createProjectTag(page, projectId, 'Important');

    await page.reload();
    await expect(page.locator(SettingsPage.section('Tags'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(page.locator(TagsPage.labelInput).first()).toHaveValue(
      'Important',
      { timeout: 15_000 },
    );
  });

  test('TG3: assigning a tag to a class shows it on the entity', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await createProjectTag(page, projectId, 'Important');

    await page.goto(project.url);
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({
      timeout: 30_000,
    });
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('Person');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('Person'))).toBeVisible({
      timeout: 15_000,
    });

    await page.locator(Hierarchy.treeNode('Person')).first().click();
    await page.locator(Hierarchy.treeNode('Person')).first().click({ button: 'right' });
    // First context-menu open awaits a permissions RPC.
    await expect(page.locator(Hierarchy.contextMenu)).toBeVisible({
      timeout: 15_000,
    });
    const tagsItem = page.locator(Hierarchy.contextMenuItem('Tags'));
    await tagsItem.hover();
    await tagsItem.click();

    await expect(page.locator(EntityTags.modal)).toBeVisible({ timeout: 15_000 });
    const checkbox = page
      .locator('.wp-modal div')
      .filter({ has: page.locator(`.wp-tag:has-text("Important")`) })
      .locator('input[type="checkbox"]')
      .last();
    await checkbox.check();
    await page.locator(Modal.primary).click();
    await page.waitForLoadState('networkidle');

    // The assigned tag renders as a chip in the editor portlet's tag list.
    await expect(
      page.locator(EntityTags.assignedTag('Important')).first(),
    ).toBeVisible({ timeout: 15_000 });
  });
});
