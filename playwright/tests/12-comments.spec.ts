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
 *
 * Scope note: resolving/reopening a thread is not covered. On this
 * deployment SetDiscussionThreadStatusAction fails server-side with a
 * JSON deserialization error, so the resolve write-path is broken in the
 * published build and can only be surfaced (by the fixture error gate),
 * not asserted green.
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
});
