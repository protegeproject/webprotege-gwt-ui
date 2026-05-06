import { test as base, expect, Page } from '@playwright/test';
import { CreateProjectDialog, ProjectList, ProjectView } from './selectors';

/**
 * Navigate to a perspective tab. "Object Properties" / "Data Properties" /
 * "Annotation Properties" live as sub-tabs under "Properties" in the same
 * PerspectiveSwitcher and only render after the parent has been opened, so
 * route through "Properties" first when targeting any of them.
 */
export async function goToPerspective(page: Page, label: string): Promise<void> {
  if (ProjectView.propertiesSubTabs.includes(label)) {
    await page.locator(ProjectView.tab('Properties')).first().click();
    // The sub-tabs ("Object Properties", "Data Properties",
    // "Annotation Properties") only attach after the parent is opened.
    await expect(page.locator(ProjectView.tab(label)).first()).toBeVisible({
      timeout: 10_000,
    });
  }
  await page.locator(ProjectView.tab(label)).first().click();
}

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
  const projectRow = page
    .locator(ProjectList.rows)
    .filter({ has: page.locator(ProjectList.nameCell, { hasText: name }) });
  await expect(projectRow).toBeVisible({ timeout: 30_000 });
  await projectRow.locator(ProjectList.nameCell).click();
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
  const trashItem = page.locator('.wp-popup-menu__item').filter({ hasText: /Move to trash/i });
  await trashItem.hover();
  await trashItem.click();
  // TrashManagerRequestHandlerImpl now fires ProjectMovedToTrashEvent on
  // dispatch success, so ProjectManagerPresenter updates the cache and
  // the row drops out of the default view. Assert it without a reload —
  // otherwise we'd be hiding any future regression of that local
  // event-fire path behind a workaround.
  await expect(row).toHaveCount(0, { timeout: 15_000 });
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
