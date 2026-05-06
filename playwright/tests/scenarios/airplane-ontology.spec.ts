import type { Page } from '@playwright/test';
import { test, expect, goToPerspective } from '../../support/fixtures';
import { addPrimitiveValue, addPropertyValue } from '../../support/frameEditor';
import {
  CreateEntityDialog,
  FrameEditor,
  Hierarchy,
  ProjectView,
} from '../../support/selectors';

/**
 * Airplane ontology scenario — issue #267.
 *
 * Builds a small but semantically connected ontology in a fresh project.
 * On top of the per-feature CRUD specs, this scenario also exercises the
 * frame editor: domain/range on properties, class restrictions, type and
 * property assertions on individuals, and an rdfs:label annotation.
 *
 * The frame editor commits ~2s after each value change (see
 * `EditorPresenter.VALUE_CHANGED_COMMIT_DELAY_MS`), so each frame edit
 * tabs out of the row and waits for the debounce to elapse before moving
 * on. Long-running: budget ~3-4 min in CI.
 */

test.describe.configure({ mode: 'serial' });

async function selectClass(page: Page, label: string): Promise<void> {
  await page.locator(ProjectView.tab('Classes')).click();
  await page.locator(Hierarchy.treeNode(label)).first().click();
}

async function createSubClass(page: Page, parent: string, child: string): Promise<void> {
  await selectClass(page, parent);
  await page.locator(Hierarchy.toolbar.create).first().click();
  await page.locator(CreateEntityDialog.name).fill(child);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(child))).toBeVisible({
    timeout: 15_000,
  });
}

async function createObjectProperty(page: Page, parent: string, name: string): Promise<void> {
  await goToPerspective(page, 'Object Properties');
  await page.locator(Hierarchy.treeNode(parent)).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await page.locator(CreateEntityDialog.name).fill(name);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(name))).toBeVisible({
    timeout: 15_000,
  });
}

async function createDataProperty(page: Page, parent: string, name: string): Promise<void> {
  await goToPerspective(page, 'Data Properties');
  await page.locator(Hierarchy.treeNode(parent)).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await page.locator(CreateEntityDialog.name).fill(name);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(name))).toBeVisible({
    timeout: 15_000,
  });
}

async function createIndividual(page: Page, name: string): Promise<void> {
  await page.locator(ProjectView.tab('Individuals')).click();
  await page.locator('button.wp-btn-g--create-individual').first().click();
  await page.locator(CreateEntityDialog.name).fill(name);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(`text=${name}`).first()).toBeVisible({ timeout: 15_000 });
}

async function selectObjectProperty(page: Page, label: string): Promise<void> {
  await goToPerspective(page, 'Object Properties');
  await page.locator(Hierarchy.treeNode(label)).first().click();
}

async function selectDataProperty(page: Page, label: string): Promise<void> {
  await goToPerspective(page, 'Data Properties');
  await page.locator(Hierarchy.treeNode(label)).first().click();
}

async function selectIndividual(page: Page, name: string): Promise<void> {
  await page.locator(ProjectView.tab('Individuals')).click();
  await page
    .locator('.wp-entity-node__display-name')
    .filter({ hasText: name })
    .first()
    .click();
}

test('builds the Airplane ontology end-to-end', async ({ page, project }) => {
  test.setTimeout(300_000);

  // ── Sub-class hierarchy ─────────────────────────────────────────────
  await createSubClass(page, 'owl:Thing', 'Vehicle');
  await createSubClass(page, 'Vehicle', 'Aircraft');
  await createSubClass(page, 'Aircraft', 'FixedWingAircraft');
  await createSubClass(page, 'Aircraft', 'Helicopter');
  await createSubClass(page, 'FixedWingAircraft', 'CommercialAircraft');
  await createSubClass(page, 'FixedWingAircraft', 'MilitaryAircraft');
  await createSubClass(page, 'owl:Thing', 'Engine');
  await createSubClass(page, 'owl:Thing', 'Wing');
  await createSubClass(page, 'owl:Thing', 'FuselagePart');

  // ── Object properties ──────────────────────────────────────────────
  await createObjectProperty(page, 'owl:topObjectProperty', 'hasPart');
  await createObjectProperty(page, 'hasPart', 'hasEngine');
  await createObjectProperty(page, 'hasPart', 'hasWing');

  // ── Data properties ────────────────────────────────────────────────
  await createDataProperty(page, 'owl:topDataProperty', 'hasWeight');
  await createDataProperty(page, 'owl:topDataProperty', 'hasMaxAltitude');

  // ── Individuals ────────────────────────────────────────────────────
  await createIndividual(page, 'Boeing747');
  await createIndividual(page, 'Boeing747_Engine_1');
  await createIndividual(page, 'Boeing747_Wing_1');
  await createIndividual(page, 'AirbusA320');
  await createIndividual(page, 'F16');
  await createIndividual(page, 'ApacheHelicopter');

  // ── Object property domain/range ───────────────────────────────────
  // hasEngine: Domain Aircraft, Range Engine.
  await selectObjectProperty(page, 'hasEngine');
  await addPrimitiveValue(page, 'Domain', 'Aircraft');
  await addPrimitiveValue(page, 'Range', 'Engine');

  // hasWing: Domain FixedWingAircraft, Range Wing.
  await selectObjectProperty(page, 'hasWing');
  await addPrimitiveValue(page, 'Domain', 'FixedWingAircraft');
  await addPrimitiveValue(page, 'Range', 'Wing');

  // ── Data property domain ───────────────────────────────────────────
  await selectDataProperty(page, 'hasWeight');
  await addPrimitiveValue(page, 'Domain', 'Aircraft');

  // ── Class restriction (Aircraft ⊑ ∃hasEngine.Engine) ───────────────
  // The Class frame's "Relationships" section interprets a property + class
  // pair as `SubClassOf <property> some <class>`.
  await selectClass(page, 'Aircraft');
  await addPropertyValue(page, 'Relationships', 'hasEngine', 'Engine');

  // ── Annotation on Aircraft (rdfs:label) ────────────────────────────
  await addPropertyValue(page, 'Annotations', 'rdfs:label', 'Aircraft');

  // ── Individual: Boeing747 type CommercialAircraft ──────────────────
  await selectIndividual(page, 'Boeing747');
  await addPrimitiveValue(page, 'Types', 'CommercialAircraft');

  // ── Object property assertion: Boeing747 hasEngine Boeing747_Engine_1 ──
  await addPropertyValue(page, 'Relationships', 'hasEngine', 'Boeing747_Engine_1');

  // ── Data property assertion: Boeing747 hasWeight 180000 ────────────
  await addPropertyValue(page, 'Relationships', 'hasWeight', '180000');

  // ── History sanity check ───────────────────────────────────────────
  // The change list groups all entries from the same day under one date
  // heading ("... 2026"), so the year only appears once. Count the
  // per-revision badges instead — ChangeDetailsViewImpl renders each as
  // an InlineLabel with text "R <n>" (optionally followed by a dropdown
  // arrow).
  await page.locator(ProjectView.tab('History')).click();
  const revisionBadges = page.getByText(/^R \d+/);
  await expect.poll(async () => revisionBadges.count(), {
    timeout: 30_000,
  }).toBeGreaterThan(15);

  // ── Reload and verify hierarchy + frame survives ───────────────────
  // After a fresh load the class tree is collapsed below owl:Thing, so
  // expand each parent on the path down to the deepest sub-class via its
  // `.gt-tree__handle` chevron before asserting visibility.
  await page.reload();
  await page.locator(ProjectView.tab('Classes')).click();
  await expect(page.locator(Hierarchy.treeNode('Vehicle'))).toBeVisible();
  for (const parent of ['Vehicle', 'Aircraft']) {
    await page
      .locator(Hierarchy.treeNode(parent))
      .locator('.gt-tree__handle')
      .click();
  }
  for (const label of ['Aircraft', 'FixedWingAircraft', 'Helicopter']) {
    await expect(page.locator(Hierarchy.treeNode(label))).toBeVisible();
  }

  // The Aircraft frame should now show the rdfs:label annotation and the
  // hasEngine relationship that we added.
  await page.locator(Hierarchy.treeNode('Aircraft')).click();
  await expect(
    page
      .locator(FrameEditor.section('Annotations'))
      .locator(FrameEditor.row)
      .filter({ hasText: 'rdfs:label' }),
  ).toContainText('Aircraft');
  await expect(
    page
      .locator(FrameEditor.section('Relationships'))
      .locator(FrameEditor.row)
      .filter({ hasText: 'hasEngine' }),
  ).toContainText('Engine');
});
