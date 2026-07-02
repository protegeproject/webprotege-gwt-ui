import { Download, Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
  ProjectList,
  ProjectView,
} from '../support/selectors';

/**
 * Ontology downloads. Both entry points open GET /download on the UI
 * server in a popup window; the servlet asks the snapshot-generator
 * service to serialize the project at a revision, fetches the resulting
 * zip from MinIO and streams it back with a Content-Disposition header.
 *
 * Requires the webprotege-snapshot-generator-service to be part of the
 * compose stack (it shares the backend's data volume).
 */

/**
 * Confirm the format dialog and capture the download it triggers.
 * The format chooser is a legacy gwt-DialogBox (not a wp-modal), and
 * ProjectRevisionDownloader calls Window.open(...), so the download event
 * fires on the popup page, not on the current one.
 */
async function confirmFormatDialogAndCaptureDownload(page: Page) {
  const dialog = page.locator('.gwt-DialogBox').filter({ hasText: 'Download project' });
  await expect(dialog).toBeVisible();
  // The download event fires on the popup page as soon as its navigation
  // turns into a download, which can beat a listener attached after the
  // popup promise resolves. Register on the context before clicking OK so
  // whichever page receives the download, we catch it.
  const downloadPromise = new Promise<Download>((resolve) => {
    page.context().once('page', (popup) => popup.once('download', resolve));
    page.once('download', resolve);
  });
  await dialog.locator('button.wp-btn--accept').click();
  const download = await Promise.race([
    downloadPromise,
    new Promise<never>((_, reject) =>
      setTimeout(() => reject(new Error('Timed out waiting for the download')), 120_000),
    ),
  ]);
  return download;
}

test.describe('download', () => {
  test('D1: Download from the project list menu delivers a zip named after the project', async ({
    page,
    project,
  }) => {
    await page.goto('/#projects/list');
    const row = page
      .locator(ProjectList.rows)
      .filter({ has: page.locator(ProjectList.nameCell, { hasText: project.name }) });
    await expect(row).toBeVisible({ timeout: 15_000 });

    await row.locator(ProjectList.menuButton).click();
    const downloadItem = page
      .locator('.wp-popup-menu__item')
      .filter({ hasText: /^Download$/ });
    await downloadItem.hover();
    await downloadItem.click();

    const download = await confirmFormatDialogAndCaptureDownload(page);
    // Default format is RDF/XML (owl); the file is named after the project.
    expect(download.suggestedFilename()).toBe(`${project.name}-ontologies.owl.zip`);

    // A zip file starts with the bytes PK.
    const stream = await download.createReadStream();
    const firstChunk: Buffer = await new Promise((resolve, reject) => {
      stream.once('data', resolve);
      stream.once('error', reject);
    });
    expect(firstChunk.subarray(0, 2).toString('latin1')).toBe('PK');
  });

  test('D2: Download revision from the History tab delivers a revision-stamped zip', async ({
    page,
    project,
  }) => {
    // Create a class so there is a second revision to download.
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await page.locator(CreateEntityDialog.name).fill('DownloadProbe');
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode('DownloadProbe'))).toBeVisible();

    await page.locator(ProjectView.tab('History')).click();
    // The newest revision renders first as an "R <n> ▾" button.
    const revisionButton = page.getByText(/^R \d+ ▾$/).first();
    await expect(revisionButton).toBeVisible({ timeout: 15_000 });
    const revisionNumber = (await revisionButton.innerText()).match(/\d+/)?.[0];
    await revisionButton.click();

    const downloadItem = page
      .locator('.wp-popup-menu__item')
      .filter({ hasText: /Download revision/ });
    await downloadItem.hover();
    await downloadItem.click();

    const download = await confirmFormatDialogAndCaptureDownload(page);
    expect(download.suggestedFilename()).toBe(
      `${project.name}-ontologies-r${revisionNumber}.owl.zip`,
    );
  });
});
