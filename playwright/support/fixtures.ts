import { test as base, expect, Page } from '@playwright/test';
import { CreateProjectDialog, ProjectList, ProjectView } from './selectors';

export interface TestProject {
  /** Unique name for the project, e.g. 'Test_5b3f...' */
  name: string;
  /** URL hash route once opened, e.g. '#projects/{uuid}/perspectives/{uuid}'. */
  url: string;
}

export interface ProjectFixtures {
  /**
   * Creates a fresh, blank project via the UI before the test runs and
   * trashes it after. The page lands on the project's default perspective
   * (Classes), so specs can begin interacting immediately.
   */
  project: TestProject;
}

const uniqueProjectName = (prefix = 'Test'): string =>
  `${prefix}_${crypto.randomUUID().slice(0, 8)}`;

async function createProjectViaUi(page: Page, name: string): Promise<TestProject> {
  await page.goto('/#projects/list');
  await page.locator(ProjectList.createButton).click();
  await page.locator(CreateProjectDialog.name).first().fill(name);
  await page.locator(CreateProjectDialog.submit).click();
  await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
  await page.waitForURL((url) => url.hash.startsWith('#projects/'));
  return { name, url: page.url() };
}

async function trashProjectViaUi(page: Page, project: TestProject): Promise<void> {
  await page.goto('/#projects/list');
  const row = page
    .locator(ProjectList.rows)
    .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) });
  if ((await row.count()) === 0) return;
  await row.locator(ProjectList.menuButton).click();
  await page.locator('role=menuitem >> text=/Trash|Move to Trash/').click();
}

export const test = base.extend<ProjectFixtures>({
  project: async ({ page }, use) => {
    const name = uniqueProjectName();
    const project = await createProjectViaUi(page, name);
    await use(project);
    await trashProjectViaUi(page, project).catch((err) => {
      // Cleanup is best-effort. A failure here should not mask the test result.
      console.warn(`[fixtures] Failed to trash project ${project.name}:`, err);
    });
  },
});

export { expect };
