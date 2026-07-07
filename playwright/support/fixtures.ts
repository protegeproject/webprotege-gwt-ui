import { test as base, expect, Page } from '@playwright/test';
import { CreateProjectDialog, ProjectList, ProjectView } from './selectors';

/**
 * Navigate to a perspective tab. "Object Properties" / "Data Properties" /
 * "Annotation Properties" live as sub-tabs under "Properties" in the same
 * PerspectiveSwitcher and only render after the parent has been opened, so
 * route through "Properties" first when targeting any of them.
 */
export async function goToPerspective(page: Page, label: string): Promise<void> {
  if ((ProjectView.propertiesSubTabs as readonly string[]).includes(label)) {
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

/** Extract the 36-char project uuid from a TestProject's opened URL. */
export function projectIdOf(project: TestProject): string {
  const match = new URL(project.url).hash.match(/projects\/([0-9a-f-]{36})/);
  if (!match) {
    throw new Error(`No project id found in URL: ${project.url}`);
  }
  return match[1];
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
    // Watch for server-error MessageBoxes the moment they appear. The
    // dispatch layer renders every backend NPE/500 as a `wp-modal`
    // containing "Internal Server Error" or the raw
    // "ActionExecutionException", but a `page.reload()` later in the
    // spec dismisses the popup, so a post-test snapshot can miss it.
    // Polling via DOM mutations catches transient popups too. The
    // underlying mutation often *did* persist, so a test that only
    // checks post-condition state (e.g. "is the child under the new
    // parent after reload?") will pass while the user actually saw a
    // 500 — this gate turns those silent backend regressions red.
    const observedErrors: string[] = [];
    // Watch for server-error MessageBoxes the moment they appear. The
    // dispatch layer renders every backend NPE/500 as a `wp-modal`
    // containing "Internal Server Error" or the raw
    // "ActionExecutionException", but a `page.reload()` later in the
    // spec dismisses the popup, so a post-test snapshot can miss it.
    // Polling via DOM mutations catches transient popups too. The
    // underlying mutation often *did* persist, so a test that only
    // checks post-condition state (e.g. "is the child under the new
    // parent after reload?") will pass while the user actually saw a
    // 500 — this gate turns those silent backend regressions red.
    await page.exposeFunction('__wpRecordError', (text: string) => {
      observedErrors.push(text);
    });
    await page.addInitScript(() => {
      const observer = new MutationObserver(() => {
        document.querySelectorAll('.wp-modal').forEach((node) => {
          const text = node.textContent ?? '';
          if (
            !(node as HTMLElement).dataset.wpErrorRecorded &&
            (text.includes('Internal Server Error') ||
              text.includes('ActionExecutionException'))
          ) {
            (node as HTMLElement).dataset.wpErrorRecorded = '1';
            (window as unknown as {
              __wpRecordError: (s: string) => void;
            }).__wpRecordError(text.trim().slice(0, 500));
          }
        });
      });
      observer.observe(document.body, { childList: true, subtree: true });
    });
    // Belt-and-braces: also peek at GWT-RPC bodies. The dispatch
    // service returns HTTP 200 even on backend NPEs — `//EX[...]` for
    // exceptions vs `//OK[...]` for success — so HTTP status alone
    // would never catch this. Tests that need this gate to fire
    // reliably must give the response time to arrive before
    // navigating (a `page.reload()` immediately after a mutation
    // cancels the in-flight RPC client-side, so its `//EX` body never
    // reaches us).
    const pendingBodies: Promise<unknown>[] = [];
    page.on('response', (response) => {
      if (!/dispatchservice/i.test(response.url())) return;
      pendingBodies.push(
        response
          .text()
          .then((body) => {
            if (body.startsWith('//EX[')) {
              observedErrors.push(
                `dispatchservice EX: ${body.slice(0, 250).replace(/\s+/g, ' ')}`,
              );
            }
          })
          .catch(() => {
            // Body read after page navigation — can't tell success
            // from failure, so stay silent rather than red-herring.
          }),
      );
    });
    const name = uniqueProjectName();
    const project = await createProjectViaUi(page, name);
    await use(project);
    // Drain any in-flight `response.text()` promises so dispatch
    // failures captured during the test propagate before the check.
    await Promise.all(pendingBodies);
    if (observedErrors.length > 0) {
      throw new Error(
        `Backend error(s) surfaced during the test: ${observedErrors.join(' || ')}`,
      );
    }
    await trashProjectViaUi(page, project).catch((err) => {
      // Cleanup is best-effort. A failure here should not mask the test result.
      console.warn(`[fixtures] Failed to trash project ${project.name}:`, err);
    });
  },
});

export { expect };
