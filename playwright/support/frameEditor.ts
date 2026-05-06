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

/** Type into a single-column list section (PrimitiveDataListEditor — used
 * for Parents, Types, Domain, Range, Same As). */
export async function addPrimitiveValue(
  page: Page,
  sectionLabel: string,
  value: string,
): Promise<void> {
  const section = page.locator(FrameEditor.section(sectionLabel));
  const row = blankRow(section);
  await row.locator('input').first().fill(value);
  await page.keyboard.press('Tab');
  await page.waitForTimeout(COMMIT_DELAY_MS);
}

/** Add a property/value row to a PropertyValueListEditor section — used
 * for "Annotations" (annotation property + literal) and "Relationships"
 * (object/data property + entity/literal). */
export async function addPropertyValue(
  page: Page,
  sectionLabel: string,
  property: string,
  value: string,
): Promise<void> {
  const section = page.locator(FrameEditor.section(sectionLabel));
  const row = blankRow(section);
  // Each PropertyValueDescriptorEditorImpl row renders three text inputs
  // in document order: property field, value field, language tag.
  await row.locator('input').nth(0).fill(property);
  await row.locator('input').nth(1).fill(value);
  await page.keyboard.press('Tab');
  await page.waitForTimeout(COMMIT_DELAY_MS);
}
