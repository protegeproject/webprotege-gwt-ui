import { test, expect } from '../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
  ProjectView,
} from '../support/selectors';

test.describe('history and comments', () => {
  test('H1: History perspective lists revisions after edits', async ({
    page,
    project,
  }) => {
    // Create a class so there is at least one revision to display.
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('HistoryProbe');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('HistoryProbe'))).toBeVisible();

    await page.locator(ProjectView.tab('History')).click();
    // Expect at least one revision row. The history portlet renders rows
    // with timestamps; we look for any element containing a 4-digit year.
    await expect(
      page.locator(`text=/${new Date().getFullYear()}/`).first(),
    ).toBeVisible({ timeout: 15_000 });
  });

  test('H3: Comments perspective opens', async ({ page, project }) => {
    await page.locator(ProjectView.tab('Comments')).click();
    await expect(page.locator(ProjectView.root)).toBeVisible();
  });
});
