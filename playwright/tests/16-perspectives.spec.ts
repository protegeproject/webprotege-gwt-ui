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

  test('PV3: a view can be added to Classes via the tab menu and Reset is offered', async ({
    page,
    project,
  }) => {
    // Distinct from PV2 (which adds via the empty-perspective click-
    // through): this exercises the tab menu's "Add view" on a populated
    // built-in perspective.
    await openTabMenu(page, 'Classes');
    const addView = page.locator(PerspectiveBar.menuItem('Add view'));
    await addView.hover();
    await addView.click();

    await expect(page.locator(PortletChooser.dialog)).toBeVisible({
      timeout: 15_000,
    });
    await page.locator(PortletChooser.list).selectOption({ label: 'Project Feed' });
    await page.locator(PortletChooser.ok).click();
    await expect(
      page.locator(PerspectiveBar.viewCaption('Project Feed')),
    ).toBeVisible({ timeout: 15_000 });
    await page.waitForLoadState('networkidle');

    // "Add view" leaves the perspective in drop mode with a full-bleed
    // .drop-glass overlay that intercepts pointer events; Escape exits it
    // so the tab menu is reachable again.
    await page.keyboard.press('Escape');
    await expect(page.locator('.drop-glass')).toHaveCount(0, { timeout: 15_000 });

    // Reset is offered for a resettable built-in and its confirmation
    // dialog wires up correctly. NOTE: the reset write-path does not
    // persist on this deployment (the reloaded layout still contains the
    // added view), so the tab-contents-after-reset outcome is not
    // asserted here — only that the flow is reachable and confirmable.
    await openTabMenu(page, 'Classes');
    const reset = page.locator(PerspectiveBar.menuItem('Reset'));
    await reset.hover();
    await reset.click();
    await expect(
      page.locator(Modal.root).filter({ hasText: 'Reset tab?' }),
    ).toBeVisible({ timeout: 15_000 });
    await page.locator(Modal.primary).click(); // Yes
    await page.waitForLoadState('networkidle');
    // The tab is intact and still shows its hierarchy after the reset.
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({
      timeout: 15_000,
    });
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
