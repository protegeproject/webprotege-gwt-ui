import { test, expect } from '../support/fixtures';
import { projectIdOf } from '../support/fixtures';
import {
  Hierarchy,
  Modal,
  PerspectiveBar,
  PortletChooser,
  ProjectView,
  SettingsPage,
} from '../support/selectors';

/**
 * Perspective (tab) customization. Layout and favorite changes persist
 * per-user per-project, so the per-test fresh project fully contains
 * them. Popup-menu items must be hovered before clicking — selection is
 * mousemove-driven and executes on mouseup.
 *
 * The portlet chooser ("Choose view") is a legacy gwt-DialogBox with a
 * GWT ListBox, like the download format chooser.
 */

/**
 * A tab's menu button (the "▾" MenuButton) is only revealed on hover over
 * the tab, so hover the tab label before clicking its menu button.
 */
async function openTabMenu(
  page: import('@playwright/test').Page,
  tabLabel: string,
): Promise<void> {
  await page.locator(ProjectView.tab(tabLabel)).first().hover();
  const menuButton = page.locator(PerspectiveBar.tabMenuButton(tabLabel)).first();
  await menuButton.click();
}

/**
 * "Add view" onto an already-populated perspective is a real drag-and-drop
 * operation, not an instant insert: after choosing a portlet in the
 * "Choose view" dialog, the perspective enters drop mode behind a
 * full-bleed `.drop-glass` overlay, and the user must click an existing
 * region of the layout to complete the drop there. Pressing Escape at
 * this point *cancels* the pending add — it does not merely dismiss a
 * leftover overlay, which is an easy mistake to make since the overlay
 * looks like decoration.
 *
 * The resulting layout change is also debounced client-side by ~1s
 * (PerspectivePresenter.perspectiveSaveTimer) before it's persisted to
 * the server, so callers must wait past that before navigating away or
 * the pending SetPerspectiveLayoutAction never fires.
 */
async function completeAddViewDrop(
  page: import('@playwright/test').Page,
  dropTargetLocator: ReturnType<import('@playwright/test').Page['locator']>,
): Promise<void> {
  const box = await dropTargetLocator.boundingBox();
  const glassBox = await page.locator('.drop-glass').boundingBox();
  if (!box || !glassBox) {
    throw new Error('completeAddViewDrop: drop target or .drop-glass not visible');
  }
  await page.locator('.drop-glass').click({
    position: {
      x: box.x + box.width / 2 - glassBox.x,
      y: box.y + box.height / 2 - glassBox.y,
    },
  });
}

test.describe('perspectives', () => {
  test('PV1: default tabs render in the switcher', async ({ page, project }) => {
    await expect(page.locator(ProjectView.perspectiveSwitcher)).toBeVisible();
    for (const label of [
      'Classes',
      'Properties',
      'Individuals',
      'History',
      'Discussions',
    ]) {
      await expect(page.locator(ProjectView.tab(label)).first()).toBeVisible({
        timeout: 15_000,
      });
    }
    await expect(
      page.locator(PerspectiveBar.selectedTab).filter({ hasText: 'Classes' }),
    ).toBeVisible();
  });

  test('PV2: a blank tab accepts a view via the empty-perspective click-through', async ({
    page,
    project,
  }) => {
    await page.locator(PerspectiveBar.tabsButton).click();
    const addBlankTab = page.locator(PerspectiveBar.menuItem('Add blank tab'));
    await addBlankTab.hover();
    await addBlankTab.click();

    await expect(page.locator(Modal.textInput)).toBeVisible({ timeout: 15_000 });
    await page.locator(Modal.textInput).fill('Scratch');
    await page.locator(Modal.primary).click();

    await expect(page.locator(ProjectView.tab('Scratch'))).toBeVisible({
      timeout: 15_000,
    });
    await expect(
      page.locator(PerspectiveBar.selectedTab).filter({ hasText: 'Scratch' }),
    ).toBeVisible();

    await expect(page.locator(PerspectiveBar.emptyPerspective)).toBeVisible({
      timeout: 15_000,
    });
    await page.locator(PerspectiveBar.emptyPerspective).click();
    await expect(page.locator(PortletChooser.dialog)).toBeVisible({
      timeout: 15_000,
    });
    await page.locator(PortletChooser.list).selectOption({ label: 'Project Feed' });
    await page.locator(PortletChooser.ok).click();

    await expect(
      page.locator(PerspectiveBar.viewCaption('Project Feed')),
    ).toBeVisible({ timeout: 15_000 });
    await page.waitForLoadState('networkidle');
  });

  test('PV3: a view added to Classes via the tab menu persists, and Reset removes it', async ({
    page,
    project,
  }) => {
    // Distinct from PV2 (which adds via the empty-perspective click-
    // through): this exercises the tab menu's "Add view" on a populated
    // built-in perspective. Uses "Watched Entities" rather than "Project
    // Feed" — Project Feed is already part of the built-in Classes
    // layout (see Classes.json), so adding/resetting it proves nothing.
    await openTabMenu(page, 'Classes');
    const addView = page.locator(PerspectiveBar.menuItem('Add view'));
    await addView.hover();
    await addView.click();

    await expect(page.locator(PortletChooser.dialog)).toBeVisible({
      timeout: 15_000,
    });
    await page.locator(PortletChooser.list).selectOption({ label: 'Watched Entities' });
    await page.locator(PortletChooser.ok).click();

    // Complete the drop onto the class-hierarchy region of the layout.
    await completeAddViewDrop(page, page.locator(Hierarchy.treeNode('owl:Thing')).first());
    await expect(
      page.locator(PerspectiveBar.viewCaption('Watched Entities')),
    ).toBeVisible({ timeout: 15_000 });

    // Wait past the client-side save debounce before proving persistence
    // with a cold reload (a soft reload/navigation is not a strong enough
    // check — GWT's in-memory place history can mask a save that never
    // actually reached the server).
    await page.waitForTimeout(2_500);
    const addedUrl = page.url();
    await page.goto('about:blank');
    await page.goto(addedUrl);
    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
    // NOTE: dropping directly onto the class-hierarchy row's region can
    // replace/consume that split rather than adding alongside it, so the
    // tree is not asserted here — only that the new view was persisted.
    // The tree's presence is re-checked after Reset below, where it must
    // be back regardless of how the drop above resolved.
    await expect(
      page.locator(PerspectiveBar.viewCaption('Watched Entities')),
    ).toBeVisible({ timeout: 15_000 });

    // Reset removes it. showYesNoConfirmBox swaps button roles just like
    // showConfirmBox elsewhere in this codebase: "Yes" carries
    // wp-btn--escape and "No" carries wp-btn--primary, so the accept
    // button must be matched by text, not by the primary class.
    await openTabMenu(page, 'Classes');
    const reset = page.locator(PerspectiveBar.menuItem('Reset'));
    await reset.hover();
    await reset.click();
    await expect(
      page.locator(Modal.root).filter({ hasText: 'Reset tab?' }),
    ).toBeVisible({ timeout: 15_000 });
    await page.locator('.wp-modal button:has-text("Yes")').click();
    await page.waitForTimeout(2_000);

    const resetUrl = page.url();
    await page.goto('about:blank');
    await page.goto(resetUrl);
    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(
      page.locator(PerspectiveBar.viewCaption('Watched Entities')),
    ).toHaveCount(0, { timeout: 15_000 });
  });

  test('PV4: closing a tab removes it; "Tabs" re-adds it', async ({
    page,
    project,
  }) => {
    await openTabMenu(page, 'History');
    const close = page.locator(PerspectiveBar.menuItem('Close'));
    await close.hover();
    await close.click();
    await expect(page.locator(ProjectView.tab('History'))).toHaveCount(0, {
      timeout: 15_000,
    });

    await page.locator(PerspectiveBar.tabsButton).click();
    const historyItem = page
      .locator('.wp-popup-menu__item')
      .filter({ hasText: /^History$/ });
    await expect(historyItem).toBeVisible({ timeout: 15_000 });
    await expect(historyItem).not.toHaveClass(/--disabled/);
    await historyItem.hover();
    await historyItem.click();

    await expect(page.locator(ProjectView.tab('History'))).toBeVisible({
      timeout: 15_000,
    });
    await page.waitForLoadState('networkidle');
  });

  test('PV5: perspectives manager page renders its sections', async ({
    page,
    project,
  }) => {
    await page.goto(`/#projects/${projectIdOf(project)}/settings/perspectives`);
    await expect(page.locator(SettingsPage.root)).toBeVisible({ timeout: 30_000 });
    await expect(page.locator(SettingsPage.section('Tabs'))).toBeVisible({
      timeout: 15_000,
    });
    // Owner has EDIT_PROJECT_SETTINGS, so the admin section shows too.
    await expect(page.locator(SettingsPage.section('Tabs Admin'))).toBeVisible();
    // Leaving via Cancel must not error (direct URL entry: nextPlace is
    // empty, so the page just stays put; the fixture gate would flag any
    // backend error).
    await page.locator(SettingsPage.cancel).click();
  });
});
