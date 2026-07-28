# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: 14-revert-revision.spec.ts >> revert revision >> RV1: reverting the class-creation revision removes the class and adds a revision
- Location: tests/14-revert-revision.spec.ts:23:7

# Error details

```
TimeoutError: locator.click: Timeout 15000ms exceeded.
Call log:
  - waiting for locator('.gt-tree__row:has(:text-is("owl:Thing"))').first()
    - locator resolved to <div draggable="true" class="gt-tree__row GAW1CN3DF0B">…</div>
  - attempting click action
    2 × waiting for element to be visible, enabled and stable
      - element is visible, enabled and stable
      - scrolling into view if needed
      - done scrolling
      - <div class="GAW1CN3DLY">…</div> intercepts pointer events
    - retrying click action
    - waiting 20ms
    2 × waiting for element to be visible, enabled and stable
      - element is visible, enabled and stable
      - scrolling into view if needed
      - done scrolling
      - <div class="GAW1CN3DLY">…</div> intercepts pointer events
    - retrying click action
      - waiting 100ms
    26 × waiting for element to be visible, enabled and stable
       - element is visible, enabled and stable
       - scrolling into view if needed
       - done scrolling
       - <div class="GAW1CN3DLY">…</div> intercepts pointer events
     - retrying click action
       - waiting 500ms

```

# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - iframe
  - iframe
  - iframe
```

# Test source

```ts
  1  | import { test, expect } from '../support/fixtures';
  2  | import {
  3  |   CreateEntityDialog,
  4  |   Hierarchy,
  5  |   ProjectView,
  6  |   Revision,
  7  | } from '../support/selectors';
  8  | 
  9  | /**
  10 |  * Reverting a revision from the Project History tab. The revision badge
  11 |  * ("R <n> ▾") opens a popup menu with "Revert changes in revision <n>";
  12 |  * confirming applies an inverse changeset as a NEW head revision (the
  13 |  * original revision stays in the history).
  14 |  *
  15 |  * Two MessageBox quirks matter here:
  16 |  *  - showConfirmBox swaps the primary/escape button classes, so the
  17 |  *    accept button must be matched by TEXT, never by wp-btn--primary.
  18 |  *  - the "have been reverted" success box must be dismissed or it
  19 |  *    intercepts every subsequent click.
  20 |  */
  21 | 
  22 | test.describe('revert revision', () => {
  23 |   test('RV1: reverting the class-creation revision removes the class and adds a revision', async ({
  24 |     page,
  25 |     project,
  26 |   }) => {
> 27 |     await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
     |                                                                 ^ TimeoutError: locator.click: Timeout 15000ms exceeded.
  28 |     await page.locator(Hierarchy.toolbar.create).first().click();
  29 |     await page.locator(CreateEntityDialog.name).fill('RevertProbe');
  30 |     await page.locator(CreateEntityDialog.submit).click();
  31 |     await expect(page.locator(Hierarchy.treeNode('RevertProbe'))).toBeVisible({
  32 |       timeout: 15_000,
  33 |     });
  34 | 
  35 |     await page.locator(ProjectView.tab('History')).click();
  36 |     const badge = page.getByText(Revision.badge).first();
  37 |     await expect(badge).toBeVisible({ timeout: 15_000 });
  38 |     const revisionNumber = Number((await badge.innerText()).match(/\d+/)?.[0]);
  39 |     expect(revisionNumber).toBeGreaterThan(0);
  40 | 
  41 |     await badge.click();
  42 |     const revertItem = page.locator(Revision.revertMenuItem);
  43 |     await revertItem.hover();
  44 |     await revertItem.click();
  45 | 
  46 |     await expect(page.locator(Revision.confirmModal)).toBeVisible({
  47 |       timeout: 15_000,
  48 |     });
  49 |     await page.locator(Revision.confirmRevert).click();
  50 | 
  51 |     await expect(page.locator(Revision.successModal)).toBeVisible({
  52 |       timeout: 20_000,
  53 |     });
  54 |     await page.locator(Revision.successOk).click();
  55 |     await expect(page.locator(Revision.successModal)).toHaveCount(0);
  56 |     await page.waitForLoadState('domcontentloaded');
  57 | 
  58 |     // The revert lands as a new head revision and the history list
  59 |     // refreshes itself (ChangeListPresenter.handleChangesReverted).
  60 |     await expect(
  61 |       page.getByText(new RegExp(`^R ${revisionNumber + 1} \\u25be$`)).first(),
  62 |     ).toBeVisible({ timeout: 15_000 });
  63 | 
  64 |     // The write-path effect: the reverted class is gone from the tree.
  65 |     // Reload rather than trusting the hierarchy event stream.
  66 |     await page.locator(ProjectView.tab('Classes')).click();
  67 |     await page.reload();
  68 |     await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({
  69 |       timeout: 30_000,
  70 |     });
  71 |     await expect(page.locator(Hierarchy.treeNode('RevertProbe'))).toHaveCount(0, {
  72 |       timeout: 15_000,
  73 |     });
  74 |   });
  75 | });
  76 | 
```