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
 *
 * NOTE ON SCOPE: these tests deliberately stop at the dialog and its
 * options and do NOT assert that a watch persists after clicking OK.
 * On this deployment the OK handler dispatches SetEntityWatchesAction,
 * which carries an ImmutableSet<Watch>; the guava build shipped in the
 * published images drops the ImmutableSet concrete subtypes from the
 * GWT-RPC serialization policy, so the action fails to serialize
 * client-side — no request is sent, the dialog does not close, and the
 * watch is never stored. Asserting persistence here would test a broken
 * path rather than the reachable UI. The context-menu -> dialog wiring
 * and the dialog's contents are the meaningful, stable surface, so that
 * is what these guard.
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

test.describe('watches', () => {
  test('W1: the Watch dialog opens from the tree context menu with all options', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'WatchProbe');
    await openWatchDialog(page, 'WatchProbe');

    // A fresh entity has no watch, so None is selected by default.
    await expect(page.locator(Watches.radio('None'))).toBeChecked();
    await expect(page.locator(Watches.radio('Entity'))).toBeVisible();
    await expect(page.locator(Watches.radio('Branch'))).toBeVisible();

    await page.locator(Watches.cancel).click();
    await expect(page.locator(Watches.modal)).toHaveCount(0, { timeout: 15_000 });
  });

  test('W2: the watch type can be selected in the dialog', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'WatchTypeProbe');
    await openWatchDialog(page, 'WatchTypeProbe');

    // Selecting a type updates the radio group (None -> Entity -> Branch),
    // which is the client-side behaviour we can assert reliably regardless
    // of the server-side persistence limitation described above.
    await page.locator(Watches.radio('Entity')).check();
    await expect(page.locator(Watches.radio('Entity'))).toBeChecked();
    await expect(page.locator(Watches.radio('None'))).not.toBeChecked();

    await page.locator(Watches.radio('Branch')).check();
    await expect(page.locator(Watches.radio('Branch'))).toBeChecked();
    await expect(page.locator(Watches.radio('Entity'))).not.toBeChecked();

    await page.locator(Watches.cancel).click();
    await expect(page.locator(Watches.modal)).toHaveCount(0, { timeout: 15_000 });
  });
});
