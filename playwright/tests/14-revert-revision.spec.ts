import { test, expect } from '../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
  ProjectView,
  Revision,
} from '../support/selectors';

/**
 * Reverting a revision from the Project History tab. The revision badge
 * ("R <n> ▾") opens a popup menu with "Revert changes in revision <n>";
 * confirming applies an inverse changeset as a NEW head revision (the
 * original revision stays in the history).
 *
 * Two MessageBox quirks matter here:
 *  - showConfirmBox swaps the primary/escape button classes, so the
 *    accept button must be matched by TEXT, never by wp-btn--primary.
 *  - the "have been reverted" success box must be dismissed or it
 *    intercepts every subsequent click.
 */

test.describe('revert revision', () => {
  test('RV1: reverting the class-creation revision removes the class and adds a revision', async ({
    page,
    project,
  }) => {
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('RevertProbe');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('RevertProbe'))).toBeVisible({
      timeout: 15_000,
    });

    await page.locator(ProjectView.tab('History')).click();
    const badge = page.getByText(Revision.badge).first();
    await expect(badge).toBeVisible({ timeout: 15_000 });
    const revisionNumber = Number((await badge.innerText()).match(/\d+/)?.[0]);
    expect(revisionNumber).toBeGreaterThan(0);

    await badge.click();
    const revertItem = page.locator(Revision.revertMenuItem);
    await revertItem.hover();
    await revertItem.click();

    await expect(page.locator(Revision.confirmModal)).toBeVisible({
      timeout: 15_000,
    });
    await page.locator(Revision.confirmRevert).click();

    await expect(page.locator(Revision.successModal)).toBeVisible({
      timeout: 20_000,
    });
    await page.locator(Revision.successOk).click();
    await expect(page.locator(Revision.successModal)).toHaveCount(0);
    await page.waitForLoadState('networkidle');

    // The revert lands as a new head revision and the history list
    // refreshes itself (ChangeListPresenter.handleChangesReverted).
    await expect(
      page.getByText(new RegExp(`^R ${revisionNumber + 1} \\u25be$`)).first(),
    ).toBeVisible({ timeout: 15_000 });

    // The write-path effect: the reverted class is gone from the tree.
    // Reload rather than trusting the hierarchy event stream.
    await page.locator(ProjectView.tab('Classes')).click();
    await page.reload();
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(page.locator(Hierarchy.treeNode('RevertProbe'))).toHaveCount(0, {
      timeout: 15_000,
    });
  });
});
