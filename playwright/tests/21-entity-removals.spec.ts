import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import {
  addPrimitiveValue,
  addPropertyValue,
  COMMIT_DELAY_MS,
} from '../support/frameEditor';
import {
  CreateEntityDialog,
  FrameEditor,
  Hierarchy,
  TagsPage,
} from '../support/selectors';

/**
 * Regression pin for #191: removed annotations/parents and deleted classes
 * must STAY removed in the UI. Project events arrive over two concurrent
 * paths (websocket push + polling safety net); before the dispatch guard
 * in EventPollingManager, a late-delivered older event window could be
 * replayed, re-adding removed hierarchy edges and resurrecting deleted
 * classes on screen even though the server data was correct. The
 * deliberate multi-poll-tick wait here gives any replay a chance to
 * happen before we assert.
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

/** Remove a frame-editor list row matched by its text and let the
 * debounced frame commit land. */
async function removeFrameRow(
  page: Page,
  sectionLabel: string,
  rowText: string,
): Promise<void> {
  const row = page
    .locator(FrameEditor.section(sectionLabel))
    .locator(FrameEditor.row)
    .filter({ hasText: rowText });
  await expect(row).toHaveCount(1);
  await row.locator(TagsPage.deleteButton).click();
  await expect(
    page
      .locator(FrameEditor.section(sectionLabel))
      .locator(FrameEditor.row)
      .filter({ hasText: rowText }),
  ).toHaveCount(0, { timeout: 15_000 });
  await page.waitForTimeout(COMMIT_DELAY_MS);
  await page.waitForLoadState('networkidle');
}

test.describe('entity removals stay removed', () => {
  test('ER1: removed annotation, removed parent, and deleted class stay gone across poll ticks and a reload', async ({
    page,
    project,
  }) => {
    // The deliberate multi-poll-tick wait plus several debounced frame
    // commits do not fit the default 60s budget.
    test.setTimeout(180_000);

    await createClassUnder(page, 'owl:Thing', 'Vehicle');
    await createClassUnder(page, 'owl:Thing', 'Machine');
    await createClassUnder(page, 'Vehicle', 'Car');
    await createClassUnder(page, 'owl:Thing', 'Disposable');

    // Delete a leaf class (C8 pattern).
    await page.locator(Hierarchy.treeNode('Disposable')).first().click();
    await page.locator(Hierarchy.toolbar.delete).first().click();
    const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
    if (await confirm.isVisible().catch(() => false)) await confirm.click();
    await expect(page.locator(Hierarchy.treeNode('Disposable'))).toHaveCount(0, {
      timeout: 15_000,
    });
    await page.waitForLoadState('networkidle');

    // Give Car an extra parent and an annotation, then remove both.
    // Keep Car selected from here on — the point is to observe the SAME
    // rendered frame across the wait, not a freshly re-fetched one.
    await page.locator(Hierarchy.treeNode('Car')).first().click();
    await addPrimitiveValue(page, 'Parents', 'Machine');
    await addPropertyValue(page, 'Annotations', 'rdfs:comment', 'ToBeRemoved');

    await removeFrameRow(page, 'Annotations', 'ToBeRemoved');
    await removeFrameRow(page, 'Parents', 'Machine');

    // Sit through at least two polling ticks (10s period): before the
    // event-replay guard this is the window in which a late poll could
    // re-fire old AddEdge/frame events and bring everything back.
    await page.waitForTimeout(25_000);

    const annotationRows = page
      .locator(FrameEditor.section('Annotations'))
      .locator(FrameEditor.row);
    const parentRows = page
      .locator(FrameEditor.section('Parents'))
      .locator(FrameEditor.row);
    await expect(annotationRows.filter({ hasText: 'ToBeRemoved' })).toHaveCount(0);
    await expect(parentRows.filter({ hasText: 'Machine' })).toHaveCount(0);
    await expect(page.locator(Hierarchy.treeNode('Disposable'))).toHaveCount(0);
    await expect(page.locator(Hierarchy.treeNode('Car'))).toBeVisible();

    // And the removals hold across a full reload (server truth).
    await page.reload();
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(page.locator(Hierarchy.treeNode('Disposable'))).toHaveCount(0);
    await page.locator(Hierarchy.treeNode('Car')).first().click();
    await expect(page.locator(FrameEditor.annotationsSection)).toBeVisible({
      timeout: 15_000,
    });
    await expect(
      page
        .locator(FrameEditor.section('Annotations'))
        .locator(FrameEditor.row)
        .filter({ hasText: 'ToBeRemoved' }),
    ).toHaveCount(0);
    await expect(
      page
        .locator(FrameEditor.section('Parents'))
        .locator(FrameEditor.row)
        .filter({ hasText: 'Machine' }),
    ).toHaveCount(0);
  });
});
