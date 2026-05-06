import type { Locator, Page } from '@playwright/test';
import { FrameEditor } from './selectors';

/**
 * Frame-editor list-section helpers.
 *
 * The Class/Property/Individual frame editors save through a debounced
 * commit timer in `EditorPresenter` (`VALUE_CHANGED_COMMIT_DELAY_MS = 2000`),
 * not on Enter or a Save button. After typing into a row we tab out so GWT
 * fires `ValueChangeEvent` on blur, then sleep slightly longer than the
 * debounce so the dispatch lands on the server before the next assertion.
 */
export const COMMIT_DELAY_MS = 2500;

/** The trailing always-empty editor row in a `ValueListFlexEditorImpl`
 * (`NewRowMode.AUTOMATIC`) — type into this row to add a new value. */
export function blankRow(section: Locator): Locator {
  return section.locator(FrameEditor.row).last();
}

/** Each PrimitiveDataEditor cell renders a `<textarea class="gwt-SuggestBox">`
 * as its visible input plus two `aria-hidden`/`opacity:0` decoy `<input>`s for
 * the image- and markdown-preview overlays. Tests must target the textarea —
 * the decoys are tabindex=-1 and accept value writes that never propagate. */
const SUGGEST_TEXTAREA = 'textarea.gwt-SuggestBox';
/** Annotation/Relationship rows append a single visible `<input class="gwt-SuggestBox" placeholder="lang">`
 * after the two cell textareas — it's a plain text box, not a SuggestBox-wrapped textarea. */
const LANG_INPUT = 'input.gwt-SuggestBox';

/** Poll until the section's row state has settled with a trailing
 * blank row. The auto-populated row (e.g. the auto `rdfs:label`)
 * arrives via a separate server callback than `ensureBlank()`, so the
 * naïve "is the last textarea empty" check can return immediately when
 * the section briefly has *only* the still-empty would-be auto-row.
 * Require the textarea to have stayed empty across consecutive polls
 * so a transient empty state during hydration can't fool us. */
async function waitForBlankRow(rowLocator: Locator): Promise<void> {
  const lastTextarea = () =>
    rowLocator.last().locator(SUGGEST_TEXTAREA).first();
  const start = Date.now();
  let consecutiveEmpty = 0;
  while (Date.now() - start < 7000) {
    let value: string | null = null;
    if ((await lastTextarea().count()) > 0) {
      value = await lastTextarea().inputValue().catch(() => null);
    }
    if (value === '') {
      consecutiveEmpty++;
      if (consecutiveEmpty >= 6) return; // ~600ms stable empty
    } else {
      consecutiveEmpty = 0;
    }
    await new Promise((r) => setTimeout(r, 100));
  }
}

/** Type into a single-column list section (PrimitiveDataListEditor — used
 * for Parents, Types, Domain, Range, Same As). */
export async function addPrimitiveValue(
  page: Page,
  sectionLabel: string,
  value: string,
): Promise<void> {
  const section = page.locator(FrameEditor.section(sectionLabel));
  await waitForBlankRow(section.locator(FrameEditor.row));
  await typeAndCommit(page, blankRow(section).locator(SUGGEST_TEXTAREA).first(), value);
  await page.waitForTimeout(COMMIT_DELAY_MS);
}

/** Drive an ExpandingTextBox's underlying SuggestBox the way a user would.
 *
 * - `click` to focus the textarea — `locator.focus()` programmatically
 *   moves DOM focus but does not fire the synthetic mouse events the
 *   GWT widget relies on to wire up its key handlers, so subsequent
 *   keystrokes silently no-op into the unfocused box.
 * - Press each character via `page.keyboard.press` with a small idle
 *   pause between strokes. `locator.pressSequentially()` and
 *   `keyboard.type()` fire the keys faster than the SuggestBox's
 *   keyup-driven oracle can settle, and the autocomplete popup ends up
 *   eating later keystrokes; explicit per-char awaits avoid that race.
 * - Press Escape to dismiss the suggestion popup if it opened — the
 *   `ExpandingTextBoxImpl.addValueChangeHandler` wrapper drops every
 *   `ValueChangeEvent` while `suggestBox.isSuggestionListShowing()` is
 *   true, so a Tab without a prior Escape can no-op silently.
 * - Tab to blur, which fires `ValueChangeEvent` →
 *   `reparsePrimitiveData` → server lookup that resolves the entered
 *   IRI to an OWL entity. */
async function typeAndCommit(
  page: Page,
  field: Locator,
  value: string,
): Promise<void> {
  await field.click();
  for (const ch of value) {
    await page.keyboard.press(charToKey(ch));
    await page.waitForTimeout(30);
  }
  await page.keyboard.press('Escape');
  await page.keyboard.press('Tab');
}

/** `keyboard.press` takes a key name (e.g. 'a', 'A', 'Shift+1'); plain
 * punctuation has to be spelled out so colons and friends register as
 * the keypress the GWT widget expects rather than as Unicode insertions. */
function charToKey(ch: string): string {
  switch (ch) {
    case ':': return 'Shift+Semicolon';
    case ' ': return 'Space';
    default: return ch;
  }
}

/** Add a property/value row to a PropertyValueListEditor section — used
 * for "Annotations" (annotation property + literal) and "Relationships"
 * (object/data property + entity/literal). Pass `lang` to populate the
 * trailing language-tag column (e.g. "en", "de") on annotation literals. */
export async function addPropertyValue(
  page: Page,
  sectionLabel: string,
  property: string,
  value: string,
  lang?: string,
): Promise<void> {
  const section = page.locator(FrameEditor.section(sectionLabel));
  // Wait for the trailing blank row to actually exist. After selecting
  // an entity, ValueListFlexEditor.ensureBlank() inserts the blank row
  // asynchronously; reading the row count too eagerly catches just the
  // server-rehydrated rows, and the helper would then type into the
  // auto-populated `rdfs:label` row by mistake — concatenating onto its
  // existing text and tripping a server NPE.
  const rowLocator = section.locator(FrameEditor.row);
  await waitForBlankRow(rowLocator);
  // Pin the row by absolute index. Once the property + value commit,
  // ensureBlank() inserts a *new* trailing row, so re-evaluating
  // `.last()` for the lang write would land on the wrong row.
  const startingRowCount = await rowLocator.count();
  const row = rowLocator.nth(startingRowCount - 1);
  // Each PropertyValueDescriptorEditorImpl row renders, in document order:
  // property textarea → value textarea → optional `lang` text input.
  await typeAndCommit(page, row.locator(SUGGEST_TEXTAREA).nth(0), property);
  await typeAndCommit(page, row.locator(SUGGEST_TEXTAREA).nth(1), value);
  if (lang) {
    await typeAndCommit(page, row.locator(LANG_INPUT), lang);
  }
  await page.waitForTimeout(COMMIT_DELAY_MS);
}
