import { test, expect } from '../support/fixtures';
import { projectIdOf } from '../support/fixtures';
import {
  PrefixesPage,
  ProjectList,
  ProjectMenu,
  SettingsPage,
} from '../support/selectors';

/**
 * Project settings, prefix declarations, and the admin application
 * settings page.
 *
 * ST4 is strictly READ-ONLY by design: Apply on #application/settings
 * writes the whole global form back — toggling "Project creation
 * enabled" would race the other workers whose project fixture is
 * creating projects, and invalid URL fields silently coerce to "".
 */

test.describe('settings', () => {
  test('ST1: project settings page renders its sections', async ({
    page,
    project,
  }) => {
    await page.locator(ProjectMenu.button).click();
    const settingsItem = page.locator(ProjectMenu.item('Settings'));
    await settingsItem.hover();
    await settingsItem.click();

    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/settings$/);
    for (const section of [
      'Main Settings',
      'New Entity Settings',
      'Display Name Settings',
      'Entity Deprecation Settings',
    ]) {
      await expect(page.locator(SettingsPage.section(section))).toBeVisible({
        timeout: 30_000,
      });
    }
    // GetProjectSettings loads async — the display name arrives last.
    await expect(
      page
        .locator(SettingsPage.section('Main Settings'))
        .locator('input.gwt-TextBox')
        .first(),
    ).toHaveValue(project.name, { timeout: 15_000 });
  });

  test('ST2: the display name is editable and Cancel discards the change', async ({
    page,
    project,
  }) => {
    const edited = `${project.name}_Edited`;

    await page.locator(ProjectMenu.button).click();
    const settingsItem = page.locator(ProjectMenu.item('Settings'));
    await settingsItem.hover();
    await settingsItem.click();

    const nameInput = page
      .locator(SettingsPage.section('Main Settings'))
      .locator('input.gwt-TextBox')
      .first();
    await expect(nameInput).toHaveValue(project.name, { timeout: 30_000 });
    // The field accepts edits.
    await nameInput.fill(edited);
    await expect(nameInput).toHaveValue(edited);

    // Cancel discards without persisting. NOTE: Apply is deliberately not
    // used — on this deployment the SetProjectSettings action is rejected
    // by the api gateway with a 400, so the rename write-path is broken;
    // the prefix write in ST3 covers a settings mutation that does persist.
    await page.locator(SettingsPage.cancel).click();
    await page.waitForURL((url) => !url.hash.endsWith('/settings'), {
      timeout: 30_000,
    });

    // The project list still shows the original name — the edit was discarded.
    await page.goto('/#projects/list');
    await expect(
      page
        .locator(ProjectList.rows)
        .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) }),
    ).toBeVisible({ timeout: 30_000 });
  });

  test('ST3: a prefix declaration persists across a reload', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await page.goto(`/#projects/${projectId}/prefixes`);
    await expect(page.locator(SettingsPage.section('Prefixes'))).toBeVisible({
      timeout: 30_000,
    });

    // Values chosen to pass validation: name ends with ':', IRI is
    // absolute and ends with '/'.
    const blankRow = page.locator(PrefixesPage.row).last();
    await blankRow.locator(PrefixesPage.nameInput).fill('veh:');
    await page.keyboard.press('Tab');
    await blankRow
      .locator(PrefixesPage.valueInput)
      .fill('http://ontologies.org/vehicles/');
    await page.keyboard.press('Tab');

    await page.locator(SettingsPage.apply).click();
    await page.waitForLoadState('networkidle');

    await page.reload();
    await expect(page.locator(SettingsPage.section('Prefixes'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(page.locator(PrefixesPage.nameInput).first()).toHaveValue(
      'veh:',
      { timeout: 15_000 },
    );
    await expect(page.locator(PrefixesPage.valueInput).first()).toHaveValue(
      'http://ontologies.org/vehicles/',
    );
  });

  test('ST4: admin application settings page renders read-only', async ({
    page,
    project,
  }) => {
    await page.goto('/#application/settings');
    // The seeded e2e user is SystemAdmin, so no ForbiddenView.
    await expect(
      page.getByText('WebProtégé Application Settings').first(),
    ).toBeVisible({ timeout: 30_000 });
    for (const section of [
      'System Settings',
      'Application URL',
      'Permissions',
      'Email Settings',
    ]) {
      await expect(page.locator(SettingsPage.section(section))).toBeVisible();
    }
    const permissions = page.locator(SettingsPage.section('Permissions'));
    for (const label of [
      'Account creation enabled',
      'Project creation enabled',
      'Project upload enabled',
    ]) {
      await expect(
        permissions.locator(`.gwt-CheckBox:has(label:has-text("${label}")) input`),
      ).toBeVisible();
    }
    // Leave via Cancel — its nextPlace is hardwired to the project list,
    // which doubles as a safe navigation assertion. DO NOT Apply here.
    await page.locator(SettingsPage.cancel).click();
    await expect(page).toHaveURL(/#projects\/list/, { timeout: 15_000 });
  });
});
