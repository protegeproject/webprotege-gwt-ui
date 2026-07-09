import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
  Watches,
} from '../support/selectors';

/**
 * Entity watches. There is no watch toolbar button — the only entry point
 * is the tree context-menu item "Watch...", which opens the Watches modal
 * with the None / Entity / Branch radio options.
 */

async function createClassUnder(
  page: Page,
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

async function openWatchDialog(page: Page, nodeLabel: string): Promise<void> {
  await page.locator(Hierarchy.treeNode(nodeLabel)).first().click();
  await page.locator(Hierarchy.treeNode(nodeLabel)).first().click({ button: 'right' });
  // The context menu is built lazily — the first open awaits a
  // GetProjectPermissionsAction round trip, so allow a generous timeout.
  await expect(page.locator(Hierarchy.contextMenu)).toBeVisible({ timeout: 15_000 });
  const item = page.locator(Hierarchy.contextMenuItem('Watch'));
  // PopupMenu selection is mousemove-driven — hover before clicking.
  await item.hover();
  await item.click();
  await expect(page.locator(Watches.modal)).toBeVisible({ timeout: 15_000 });
}

async function setWatchType(
  page: Page,
  type: 'None' | 'Entity' | 'Branch',
): Promise<void> {
  await page.locator(Watches.radio(type)).check();
  await page.locator(Watches.ok).click();
  await expect(page.locator(Watches.modal)).toHaveCount(0, { timeout: 15_000 });
  // Drain the SetEntityWatches RPC for the backend-error gate.
  await page.waitForLoadState('networkidle');
}

test.describe('watches', () => {
  test('W1: setting an entity watch persists across dialog reopens', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'WatchProbe');

    await openWatchDialog(page, 'WatchProbe');
    await expect(page.locator(Watches.radio('None'))).toBeChecked();

    await setWatchType(page, 'Entity');

    await openWatchDialog(page, 'WatchProbe');
    await expect(page.locator(Watches.radio('Entity'))).toBeChecked();
    await expect(page.locator(Watches.radio('None'))).not.toBeChecked();
    await page.locator(Watches.cancel).click();
  });

  test('W2: clearing the watch persists', async ({ page, project }) => {
    await createClassUnder(page, 'owl:Thing', 'UnwatchProbe');

    await openWatchDialog(page, 'UnwatchProbe');
    await setWatchType(page, 'Entity');

    await openWatchDialog(page, 'UnwatchProbe');
    await expect(page.locator(Watches.radio('Entity'))).toBeChecked();
    await setWatchType(page, 'None');

    await openWatchDialog(page, 'UnwatchProbe');
    await expect(page.locator(Watches.radio('None'))).toBeChecked();
    await expect(page.locator(Watches.radio('Entity'))).not.toBeChecked();
    await page.locator(Watches.cancel).click();
  });
});
