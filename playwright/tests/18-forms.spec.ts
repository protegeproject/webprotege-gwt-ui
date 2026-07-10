import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import { projectIdOf } from '../support/fixtures';
import {
  FormsPage,
  ProjectMenu,
  SettingsPage,
} from '../support/selectors';

/**
 * Forms manager and the form editor page. The manager's Apply button is
 * relabelled "OK" (setApplyButtonText), so SettingsPage.apply matches by
 * class. CAUTION: blurring the form-label input immediately fires
 * updateForm — the form exists server-side even before OK is clicked.
 */

async function openFormsPage(page: Page, projectId: string): Promise<void> {
  await page.goto(`/#projects/${projectId}/forms`);
  // A cold hash-goto to this page renders reliably under 30s locally, but
  // CI runners are slower/more contended; give it more headroom there
  // (FM2/FM3 timed out at 30s in CI while passing consistently at that
  // bound locally).
  await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
    timeout: 45_000,
  });
}

async function addForm(page: Page, label: string): Promise<void> {
  await page.locator(FormsPage.addFormButton).click();
  await expect(page.locator(FormsPage.formDetailsButton).first()).toBeVisible({
    timeout: 15_000,
  });
  const labelInput = page.locator(FormsPage.labelValueInput).first();
  await labelInput.fill(label);
  await page.keyboard.press('Tab');
  // Blur fires updateForm right away — let it land before proceeding.
  await page.waitForLoadState('networkidle');
}

test.describe('forms', () => {
  test('FM1: Forms page opens from the Project menu with its toolbar', async ({
    page,
    project,
  }) => {
    await page.locator(ProjectMenu.button).click();
    const formsItem = page.locator(ProjectMenu.item('Forms'));
    await formsItem.hover();
    await formsItem.click();

    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/forms$/);
    await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(page.locator(FormsPage.addFormButton)).toBeVisible();
    await expect(page.locator(FormsPage.copyButton)).toBeVisible();
    await expect(page.locator(FormsPage.exportButton)).toBeVisible();
    await expect(page.locator(FormsPage.importButton)).toBeVisible();
  });

  test('FM2: "Add form" adds an editable form whose label defaults to English and persists', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await openFormsPage(page, projectId);

    // A fresh project has no forms.
    await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(0);

    await page.locator(FormsPage.addFormButton).click();

    // The new form renders as an editable object-list entry: a label
    // field plus a "Form details..." button that opens its editor.
    await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(1, {
      timeout: 15_000,
    });
    const labelInput = page.locator(FormsPage.labelValueInput).first();
    await expect(labelInput).toBeVisible();
    await labelInput.fill('TestForm');
    await page.keyboard.press('Tab');
    // Blur fires updateForm immediately. #281: a label typed with no
    // explicit language tag now defaults to "en" instead of being
    // silently dropped.
    await page.waitForLoadState('networkidle');
    await expect(labelInput).toHaveValue('TestForm');

    // Persists across a reload, language tag included.
    await page.reload();
    await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
      timeout: 45_000,
    });
    await expect(page.locator(FormsPage.labelValueInput).first()).toHaveValue(
      'TestForm',
      { timeout: 15_000 },
    );
    await expect(page.locator(FormsPage.labelLangInput).first()).toHaveValue('en');
  });

  test('FM4: a form label with an explicit language tag persists as given', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await openFormsPage(page, projectId);

    await page.locator(FormsPage.addFormButton).click();
    await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(1, {
      timeout: 15_000,
    });

    // Set the language tag first: if the value field committed first with
    // the tag still blank, the #281 default-fill would set it to "en"
    // before we get a chance to type "fr" over it.
    const langInput = page.locator(FormsPage.labelLangInput).first();
    await langInput.click();
    await langInput.pressSequentially('fr', { delay: 30 });
    await page.keyboard.press('Escape');
    await page.keyboard.press('Tab');

    const labelInput = page.locator(FormsPage.labelValueInput).first();
    await labelInput.fill('FormulaireTest');
    await page.keyboard.press('Tab');
    await page.waitForLoadState('networkidle');

    await page.reload();
    await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
      timeout: 45_000,
    });
    await expect(page.locator(FormsPage.labelValueInput).first()).toHaveValue(
      'FormulaireTest',
      { timeout: 15_000 },
    );
    await expect(page.locator(FormsPage.labelLangInput).first()).toHaveValue('fr');
  });

  test('FM3: "Form details..." opens the form editor page', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await openFormsPage(page, projectId);
    await addForm(page, 'DetailsForm');

    await page.locator(FormsPage.formDetailsButton).first().click();
    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/forms\/[0-9a-f-]{36}/, {
      timeout: 15_000,
    });
    await expect(page.locator(SettingsPage.section('Forms'))).toBeVisible({
      timeout: 30_000,
    });
    await expect(
      page.locator(SettingsPage.section('Form activation')),
    ).toBeVisible();
    await expect(page.locator('button:has-text("Add field")')).toBeVisible();
  });
});
