import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import {
  CreateEntityDialog,
  FrameEditor,
  Hierarchy,
  Modal,
  ProjectList,
  ProjectView,
  TopBar,
} from '../support/selectors';

/**
 * Deep linking and navigation. ProjectViewPlaceTokenizer serializes the
 * selection as `?selection=Type(content)` appended to the perspective
 * hash route; `owl:` is the only registered prefix for prefixed names,
 * generated IRIs appear angle-bracket-quoted (and percent-encoded in
 * page.url()).
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

test.describe('navigation', () => {
  test('NV1: a static deep link with a prefixed-name token selects owl:Thing', async ({
    page,
    project,
  }) => {
    await page.goto(`${project.url}?selection=Class(owl:Thing)`);
    await expect(
      page.locator(Hierarchy.selectedNode).filter({ hasText: 'owl:Thing' }),
    ).toBeVisible({ timeout: 30_000 });
    await expect(page.locator(FrameEditor.annotationsSection)).toBeVisible({
      timeout: 15_000,
    });
  });

  test('NV2: the selection URL round-trips as a cold-load deep link', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'DeepLinkTarget');
    await page.locator(Hierarchy.treeNode('DeepLinkTarget')).first().click();
    await page.waitForURL(/\?selection=Class\(/, { timeout: 15_000 });
    const deepLink = page.url();

    // Force a genuine cold GWT bootstrap rather than a hashchange.
    await page.goto('about:blank');
    await page.goto(deepLink);

    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
    // graphtree reveals the ancestors of the selection key, so the node
    // is both present and marked selected once the tree settles.
    await expect(
      page.locator(Hierarchy.selectedNode).filter({ hasText: 'DeepLinkTarget' }),
    ).toBeVisible({ timeout: 15_000 });
    await expect(page.locator(FrameEditor.annotationsSection)).toBeVisible();
  });

  test('NV3: "Show Direct Link" exposes a shareable URL', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'LinkTarget');
    await page.locator(Hierarchy.treeNode('LinkTarget')).first().click();
    await page.locator(Hierarchy.treeNode('LinkTarget')).first().click({ button: 'right' });
    await expect(page.locator(Hierarchy.contextMenu)).toBeVisible({
      timeout: 15_000,
    });
    const linkItem = page.locator(Hierarchy.contextMenuItem('Show Direct Link'));
    await linkItem.hover();
    await linkItem.click();

    const modal = page.locator(Modal.root).filter({ hasText: 'Direct Link' });
    await expect(modal).toBeVisible({ timeout: 15_000 });
    const link = await modal.locator('textarea').inputValue();
    // The dialog serializes the place as ?fragment=<urlencoded hash token>.
    expect(link).toMatch(/\?fragment=projects%2F[0-9a-f-]{36}%2Fperspectives%2F/);
    expect(link).toContain('selection');
    await page.locator(Modal.primary).click();
    await expect(modal).toHaveCount(0);
  });

  test('NV4: the Home button returns to the project list', async ({
    page,
    project,
  }) => {
    await page.locator(TopBar.homeButton).click();
    await expect(page).toHaveURL(/#projects\/list/, { timeout: 15_000 });
    await expect(page.locator(ProjectList.root)).toBeVisible({ timeout: 15_000 });

    // Place history integration: back returns into the project.
    await page.goBack();
    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
  });

  test('NV5: the selection survives a perspective switch', async ({
    page,
    project,
  }) => {
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.waitForURL(/\?selection=Class\(owl:Thing\)/, { timeout: 15_000 });

    await page.locator(ProjectView.tab('History')).click();
    await page.locator(ProjectView.tab('Classes')).click();

    await expect(page).toHaveURL(/\?selection=Class\(owl:Thing\)/, {
      timeout: 15_000,
    });
    await expect(
      page.locator(Hierarchy.selectedNode).filter({ hasText: 'owl:Thing' }),
    ).toBeVisible({ timeout: 15_000 });
  });
});
