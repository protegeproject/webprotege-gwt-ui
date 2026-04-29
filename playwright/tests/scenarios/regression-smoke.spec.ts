import { test, expect } from '../../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
  ProjectView,
} from '../../support/selectors';

/**
 * Fast (<30s) sanity scenario. Run with `npm run test:smoke` to get a
 * yes/no signal on whether the UI is alive across the major surfaces
 * before investing in the full atomic suite.
 */

test('login → create project → create class → check history', async ({
  page,
  project,
}) => {
  // The `project` fixture already brought us to a freshly-created project
  // sitting on the Classes perspective. Just create one class.
  await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  await page.locator(CreateEntityDialog.name).fill('SmokeProbeClass');
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode('SmokeProbeClass'))).toBeVisible();

  // History perspective should now show at least the project-create and
  // class-create revisions.
  await page.locator(ProjectView.tab('History')).click();
  await expect(page.locator(ProjectView.root)).toBeVisible();
  await expect(
    page.locator(`text=/${new Date().getFullYear()}/`).first(),
  ).toBeVisible({ timeout: 15_000 });
});
