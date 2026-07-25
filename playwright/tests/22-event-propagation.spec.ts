import * as path from 'path';
import { test, expect } from '../support/fixtures';
import { CreateEntityDialog, Hierarchy, ProjectView } from '../support/selectors';

/**
 * End-to-end coverage for the SSE event transport (#303: #296/#297/#300/#304/#306).
 *
 * Requires the epic-branch images in the stack: the gateway serving
 * /data/projects/{id}/events with ticket auth, the event-history service
 * assigning per-project sequence numbers, and the client on EventSource.
 *
 * SSE1 kills the historical "channel dies after the first push" bug: with
 * hardcoded event tags, viewer B received exactly one live update per session
 * and everything after arrived via the (now removed) 10-second poll.
 */

const STORAGE_STATE = path.join(__dirname, '..', '.auth', 'storageState.json');

async function createClassUnder(page: import('@playwright/test').Page, parentLabel: string, newLabel: string): Promise<void> {
  await page.locator(Hierarchy.treeNode(parentLabel)).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  await page.locator(CreateEntityDialog.name).fill(newLabel);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(newLabel))).toBeVisible({ timeout: 15_000 });
}

test.describe('SSE event propagation', () => {
  test('SSE1: a second viewer receives successive changes live, without reload', async ({ page, browser, project }) => {
    const contextB = await browser.newContext({ storageState: STORAGE_STATE });
    const pageB = await contextB.newPage();
    await pageB.goto(project.url);
    await expect(pageB.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
    await expect(pageB.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({ timeout: 15_000 });

    // Two successive changes must BOTH arrive — the second one is the one the
    // old transport always lost.
    await createClassUnder(page, 'owl:Thing', 'PropagatedAlpha');
    await expect(pageB.locator(Hierarchy.treeNode('PropagatedAlpha'))).toBeVisible({ timeout: 10_000 });

    await createClassUnder(page, 'owl:Thing', 'PropagatedBeta');
    await expect(pageB.locator(Hierarchy.treeNode('PropagatedBeta'))).toBeVisible({ timeout: 10_000 });

    await contextB.close();
  });

  test('SSE2: a disconnected viewer catches up exactly once on reconnect', async ({ page, browser, project }) => {
    const contextB = await browser.newContext({ storageState: STORAGE_STATE });
    const pageB = await contextB.newPage();
    await pageB.goto(project.url);
    await expect(pageB.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({ timeout: 30_000 });

    // Warm the live channel so B's stream is known-good before the drop.
    await createClassUnder(page, 'owl:Thing', 'BeforeDrop');
    await expect(pageB.locator(Hierarchy.treeNode('BeforeDrop'))).toBeVisible({ timeout: 10_000 });

    await contextB.setOffline(true);
    await createClassUnder(page, 'owl:Thing', 'WhileOffline');
    // Give the disconnected stream time to notice it is gone.
    await pageB.waitForTimeout(2_000);
    await contextB.setOffline(false);

    // Recovery can take two shapes: a browser-driven reconnect resuming via
    // Last-Event-ID, or -- when the drop leaves a half-open zombie connection
    // that never errors -- the client's own liveness check, which notices the
    // silent stream after ~2.5 missed heartbeats and reopens it with the last
    // seen id. The budget covers the slower path. Nothing missing...
    await expect(pageB.locator(Hierarchy.treeNode('WhileOffline'))).toBeVisible({ timeout: 90_000 });
    // ...and nothing duplicated.
    await expect(pageB.locator(Hierarchy.treeNode('WhileOffline'))).toHaveCount(1);

    await contextB.close();
  });

  test('SSE3: an oversized change shows the refresh prompt instead of an event flood', async ({ page, browser, project }) => {
    const contextB = await browser.newContext({ storageState: STORAGE_STATE });
    const pageB = await contextB.newPage();
    await pageB.goto(project.url);
    await expect(pageB.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({ timeout: 30_000 });

    // One dialog submission with many names lands as a single revision whose
    // change count exceeds webprotege.events.largeChangeThreshold (200), so
    // the backend announces one large-number-of-changes signal instead.
    const names = Array.from({ length: 250 }, (_, i) => `Bulk_${String(i).padStart(3, '0')}`).join('\n');
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill(names);
    await page.locator(CreateEntityDialog.submit).click();

    // The other viewer is prompted to refresh...
    const refreshPrompt = pageB.locator('.wp-modal', { hasText: 'A large number of changes' });
    await expect(refreshPrompt).toBeVisible({ timeout: 30_000 });
    await refreshPrompt.locator('button', { hasText: 'Yes' }).click();
    // ...and after accepting, the reloaded view reflects the bulk change.
    await expect(pageB.locator(Hierarchy.treeNode('Bulk_000'))).toBeVisible({ timeout: 30_000 });

    // The acting user gets the same prompt for their own bulk change; dismiss
    // it so the fixture's teardown is not blocked by an open modal.
    const ownPrompt = page.locator('.wp-modal', { hasText: 'A large number of changes' });
    if (await ownPrompt.isVisible({ timeout: 5_000 }).catch(() => false)) {
      await ownPrompt.locator('button', { hasText: 'No' }).click();
    }

    await contextB.close();
  });
});
