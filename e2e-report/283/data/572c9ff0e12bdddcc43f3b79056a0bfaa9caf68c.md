# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: 18-forms.spec.ts >> forms >> FM3: "Form details..." opens the form editor page
- Location: tests/18-forms.spec.ts:88:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('.wp-settings__section:has(.wp-settings__section__title:text-is("Project Forms"))')
Expected: visible
Timeout: 45000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 45000ms
  - waiting for locator('.wp-settings__section:has(.wp-settings__section__title:text-is("Project Forms"))')

```

# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - iframe
  - iframe
  - iframe
  - generic [ref=e7]:
    - generic [ref=e10]:
      - generic [ref=e11]: Projects
      - button "e2e@test.local ▾" [ref=e14] [cursor=pointer]
    - generic [ref=e15]:
      - generic [ref=e16]:
        - button "Create New Project" [ref=e17] [cursor=pointer]
        - generic [ref=e19]:
          - generic [ref=e21]:
            - checkbox "Owned by Me" [checked] [ref=e22]
            - text: Owned by Me
          - generic [ref=e24]:
            - checkbox "Shared with Me" [checked] [ref=e25]
            - text: Shared with Me
          - generic [ref=e27]:
            - checkbox "Trash" [ref=e28]
            - text: Trash
        - combobox [ref=e29]:
          - option "Sort by Last Opened" [selected]
          - option "Sort by Last Modified"
          - option "Sort by Project Name"
          - option "Sort by Owner"
      - generic [ref=e31]:
        - generic [ref=e32]: Project name
        - generic [ref=e33]: Owner
        - generic [ref=e34]: Last opened
        - generic [ref=e35]: Last modified
```

# Test source

```ts
  1   | import { Page } from '@playwright/test';
  2   | import { test, expect } from '../support/fixtures';
  3   | import { projectIdOf } from '../support/fixtures';
  4   | import {
  5   |   FormsPage,
  6   |   ProjectMenu,
  7   |   SettingsPage,
  8   | } from '../support/selectors';
  9   | 
  10  | /**
  11  |  * Forms manager and the form editor page. The manager's Apply button is
  12  |  * relabelled "OK" (setApplyButtonText), so SettingsPage.apply matches by
  13  |  * class. CAUTION: blurring the form-label input immediately fires
  14  |  * updateForm — the form exists server-side even before OK is clicked.
  15  |  */
  16  | 
  17  | async function openFormsPage(page: Page, projectId: string): Promise<void> {
  18  |   await page.goto(`/#projects/${projectId}/forms`);
  19  |   // A cold hash-goto to this page renders reliably under 30s locally, but
  20  |   // CI runners are slower/more contended; give it more headroom there
  21  |   // (FM2/FM3 timed out at 30s in CI while passing consistently at that
  22  |   // bound locally).
> 23  |   await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
      |                                                                     ^ Error: expect(locator).toBeVisible() failed
  24  |     timeout: 45_000,
  25  |   });
  26  | }
  27  | 
  28  | async function addForm(page: Page, label: string): Promise<void> {
  29  |   await page.locator(FormsPage.addFormButton).click();
  30  |   await expect(page.locator(FormsPage.formDetailsButton).first()).toBeVisible({
  31  |     timeout: 15_000,
  32  |   });
  33  |   const labelInput = page.locator(FormsPage.labelValueInput).first();
  34  |   await labelInput.fill(label);
  35  |   await page.keyboard.press('Tab');
  36  |   // Blur fires updateForm right away — let it land before proceeding.
  37  |   await page.waitForLoadState('networkidle');
  38  | }
  39  | 
  40  | test.describe('forms', () => {
  41  |   test('FM1: Forms page opens from the Project menu with its toolbar', async ({
  42  |     page,
  43  |     project,
  44  |   }) => {
  45  |     await page.locator(ProjectMenu.button).click();
  46  |     const formsItem = page.locator(ProjectMenu.item('Forms'));
  47  |     await formsItem.hover();
  48  |     await formsItem.click();
  49  | 
  50  |     await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/forms$/);
  51  |     await expect(page.locator(SettingsPage.section('Project Forms'))).toBeVisible({
  52  |       timeout: 30_000,
  53  |     });
  54  |     await expect(page.locator(FormsPage.addFormButton)).toBeVisible();
  55  |     await expect(page.locator(FormsPage.copyButton)).toBeVisible();
  56  |     await expect(page.locator(FormsPage.exportButton)).toBeVisible();
  57  |     await expect(page.locator(FormsPage.importButton)).toBeVisible();
  58  |   });
  59  | 
  60  |   test('FM2: "Add form" adds an editable form to the list', async ({
  61  |     page,
  62  |     project,
  63  |   }) => {
  64  |     const projectId = projectIdOf(project);
  65  |     await openFormsPage(page, projectId);
  66  | 
  67  |     // A fresh project has no forms.
  68  |     await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(0);
  69  | 
  70  |     await page.locator(FormsPage.addFormButton).click();
  71  | 
  72  |     // The new form renders as an editable object-list entry: a label
  73  |     // field plus a "Form details..." button that opens its editor.
  74  |     await expect(page.locator(FormsPage.formDetailsButton)).toHaveCount(1, {
  75  |       timeout: 15_000,
  76  |     });
  77  |     const labelInput = page.locator(FormsPage.labelValueInput).first();
  78  |     await expect(labelInput).toBeVisible();
  79  |     await labelInput.fill('TestForm');
  80  |     await expect(labelInput).toHaveValue('TestForm');
  81  | 
  82  |     // NOTE: the form label is deliberately not asserted across a reload.
  83  |     // On this deployment the form-label LanguageMap is not stored (the
  84  |     // form row persists but its label comes back empty, with or without a
  85  |     // language tag), so a persistence assertion would test a broken path.
  86  |   });
  87  | 
  88  |   test('FM3: "Form details..." opens the form editor page', async ({
  89  |     page,
  90  |     project,
  91  |   }) => {
  92  |     const projectId = projectIdOf(project);
  93  |     await openFormsPage(page, projectId);
  94  |     await addForm(page, 'DetailsForm');
  95  | 
  96  |     await page.locator(FormsPage.formDetailsButton).first().click();
  97  |     await expect(page).toHaveURL(/#projects\/[0-9a-f-]{36}\/forms\/[0-9a-f-]{36}/, {
  98  |       timeout: 15_000,
  99  |     });
  100 |     await expect(page.locator(SettingsPage.section('Forms'))).toBeVisible({
  101 |       timeout: 30_000,
  102 |     });
  103 |     await expect(
  104 |       page.locator(SettingsPage.section('Form activation')),
  105 |     ).toBeVisible();
  106 |     await expect(page.locator('button:has-text("Add field")')).toBeVisible();
  107 |   });
  108 | });
  109 | 
```