import { test, expect } from '../support/fixtures';
import { CreateEntityDialog, Hierarchy, Search } from '../support/selectors';

test.describe('search', () => {
  test('S1+S2: open search modal and find a class', async ({ page, project }) => {
    // Seed a class so search has something to find.
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('Aircraft');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('Aircraft'))).toBeVisible();

    // Open the search modal. The trigger varies by skin — try a few.
    const trigger = page.locator(Search.trigger).first();
    if (await trigger.isVisible().catch(() => false)) {
      await trigger.click();
    } else {
      await page.keyboard.press('Control+Alt+f');
    }
    await expect(page.locator(Search.modal)).toBeVisible();

    await page.locator(Search.input).first().fill('Aircraft');
    await expect(page.locator(Search.result).first()).toBeVisible({
      timeout: 10_000,
    });
  });
});
