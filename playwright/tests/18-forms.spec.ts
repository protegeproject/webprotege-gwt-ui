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

  test('FM5: clicking OK right after typing a label does not revert it', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await openFormsPage(page, projectId);

    await page.locator(FormsPage.addFormButton).click();
    await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(1, {
      timeout: 15_000,
    });

    // Click OK directly, with no Tab first: the label's blur-triggered
    // per-row save (updateForm) and the page-level OK button's bulk
    // save (setForms, from every row's cached FormDescriptor) both fire
    // from this one click. If the row's cache wasn't refreshed after the
    // label edit, OK's bulk save re-sends the stale, label-less snapshot
    // and reverts what updateForm just persisted.
    const labelInput = page.locator(FormsPage.labelValueInput).first();
    await labelInput.click();
    await labelInput.fill('OkClickForm');
    await page.locator(SettingsPage.apply).click();
    await page.waitForLoadState('networkidle');

    await openFormsPage(page, projectId);
    await expect(page.locator(FormsPage.labelValueInput).first()).toHaveValue(
      'OkClickForm',
      { timeout: 15_000 },
    );
    await expect(page.locator(FormsPage.labelLangInput).first()).toHaveValue('en');
  });

  test('FM6: clicking "Form details..." right after typing a label opens the editor immediately', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    await openFormsPage(page, projectId);

    await page.locator(FormsPage.addFormButton).click();
    await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(1, {
      timeout: 15_000,
    });

    // No Tab, no wait: the label's blur (from this very click moving focus
    // away) used to synchronously insert a second blank label row before
    // the click's hit-test ran, since the label editor auto-grows in
    // NewRowMode.AUTOMATIC. That shifted "Form details..." down and
    // swallowed the click into the new row instead of opening the editor.
    const labelInput = page.locator(FormsPage.labelValueInput).first();
    await labelInput.click();
    await labelInput.fill('ImmediateDetailsForm');
    await page.locator(FormsPage.formDetailsButton).first().click();

    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/forms\/[0-9a-f-]{36}/, {
      timeout: 15_000,
    });
  });

  test('FM7: OK navigates back to the page you came from', async ({
    page,
    project,
  }) => {
    // Reached via the Project menu (not a direct goto): the app records the
    // originating place in-memory so OK knows where to return.
    await page.locator(ProjectMenu.button).click();
    const formsItem = page.locator(ProjectMenu.item('Forms'));
    await formsItem.hover();
    await formsItem.click();
    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/forms$/);
    await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
      timeout: 30_000,
    });

    await addForm(page, 'NavBackForm');
    await page.locator(SettingsPage.apply).click();

    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/perspectives\//, {
      timeout: 15_000,
    });
  });

  test('FM8: OK falls back to the project view when there is no recorded return place', async ({
    page,
    project,
  }) => {
    const projectId = projectIdOf(project);
    // Reached via a direct URL (goto): FormsPlaceTokenizer can't reconstruct
    // an in-memory "return to" place from a URL, so this exercises the
    // fallback path rather than the recorded-nextPlace path FM7 covers.
    await openFormsPage(page, projectId);

    await addForm(page, 'NoBackPlaceForm');
    await page.locator(SettingsPage.apply).click();

    // Previously a no-op: the URL stayed on /forms. Now falls back to the
    // project's default view instead of doing nothing.
    await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/perspectives\//, {
      timeout: 15_000,
    });
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
