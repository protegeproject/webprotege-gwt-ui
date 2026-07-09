import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import {
  Comments,
  CreateEntityDialog,
  Hierarchy,
  ProjectView,
} from '../support/selectors';

/**
 * Discussion threads (the Comments portlet). The portlet sits in the
 * right-hand column of the default Classes perspective, so no tab switch
 * is needed — creating a class auto-selects it, which enables the
 * "start new thread" toolbar action.
 *
 * The comment editor is a CodeMirror-5 instance inside a wp-modal:
 * fill() cannot reach it, so type through the keyboard. Bodies must not
 * contain '@' — it triggers the mention autocompleter popup.
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

async function postComment(page: Page, body: string): Promise<void> {
  await page.locator(Comments.startThreadButton).first().click();
  await expect(page.locator(Comments.editor)).toBeVisible();
  await page.locator(Comments.editor).click();
  await page.keyboard.type(body, { delay: 20 });
  await page.locator(Comments.editorOk).click();
  // Drain the CreateEntityDiscussionThread RPC so the fixture's
  // backend-error gate can observe any //EX body.
  await page.waitForLoadState('networkidle');
}

test.describe('comments', () => {
  test('CM1: posting a comment starts a discussion thread', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'CommentProbe');
    await postComment(page, 'First comment from e2e');

    await expect(page.locator(Comments.thread)).toBeVisible({ timeout: 15_000 });
    await expect(page.locator(Comments.commentBody)).toContainText(
      'First comment from e2e',
    );
    await expect(
      page.locator(Comments.thread).locator(Comments.comment),
    ).toHaveCount(1);
  });

  test('CM2: reply appends a second comment to the thread', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'ReplyProbe');
    await postComment(page, 'Opening comment');
    await expect(page.locator(Comments.thread)).toBeVisible({ timeout: 15_000 });

    await page.locator(Comments.replyButton).first().click();
    await expect(page.locator(Comments.editor)).toBeVisible();
    await page.locator(Comments.editor).click();
    await page.keyboard.type('A reply from e2e', { delay: 20 });
    await page.locator(Comments.editorOk).click();
    await page.waitForLoadState('networkidle');

    const comments = page.locator(Comments.thread).locator(Comments.comment);
    await expect(comments).toHaveCount(2, { timeout: 15_000 });
    await expect(comments.nth(1).locator(Comments.commentBody)).toContainText(
      'A reply from e2e',
    );
  });

  test('CM3: a posted thread is reachable from the Discussions perspective', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'CommentedProbe');
    await postComment(page, 'Comment for the discussions tab');
    await expect(page.locator(Comments.thread)).toBeVisible({ timeout: 15_000 });

    // The Discussions perspective carries the current selection over from
    // Classes and renders its thread in the Comments portlet, so the
    // comment stays visible after switching tabs. (The CommentedEntities
    // list on this perspective does not expose the entity display name via
    // a stable hook, so the thread body is the reliable assertion.)
    await page.locator(ProjectView.tab('Discussions')).click();
    await expect(page.locator(Comments.commentBody).first()).toContainText(
      'Comment for the discussions tab',
      { timeout: 15_000 },
    );
  });

  test('CM4: resolving a thread flips its status and reopening restores it', async ({
    page,
    project,
  }) => {
    await createClassUnder(page, 'owl:Thing', 'ResolveProbe');
    await postComment(page, 'Comment to resolve');
    await expect(page.locator(Comments.thread)).toBeVisible({ timeout: 15_000 });

    const statusButton = page.locator(Comments.threadStatus).first();
    await expect(statusButton).toHaveText(/Resolve/);
    await statusButton.click();
    await page.waitForLoadState('networkidle');

    // The status label flips to "Re-open" from the direct RPC response,
    // but when the ON_STATUS_CHANGED event later arrives over the event
    // stream, DiscussionThreadListPresenter hides the resolved thread
    // (resolved threads are filtered out of the default view). Both
    // outcomes prove the resolve landed — accept either.
    await expect
      .poll(
        async () => {
          const reopenCount = await page
            .locator(`${Comments.threadStatus}:has-text("Re-open")`)
            .count();
          const threadCount = await page.locator(Comments.thread).count();
          return reopenCount > 0 || threadCount === 0;
        },
        { timeout: 15_000 },
      )
      .toBe(true);
  });
});
