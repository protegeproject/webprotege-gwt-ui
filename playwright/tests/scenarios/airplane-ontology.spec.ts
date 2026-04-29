import type { Page } from '@playwright/test';
import { test, expect } from '../../support/fixtures';
import {
  CreateEntityDialog,
  Hierarchy,
  ProjectView,
} from '../../support/selectors';

/**
 * Airplane ontology scenario — issue #267.
 *
 * Builds ~40 axioms in a fresh project, exercising the atomic actions
 * tested individually in earlier specs. Runs sequentially. Long-running:
 * budget ~90s on a developer laptop, more in CI.
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
  await page.locator(ProjectView.tab('Object Properties')).click();
  await page.locator(Hierarchy.treeNode(parent)).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await page.locator(CreateEntityDialog.name).fill(name);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(name))).toBeVisible({
    timeout: 15_000,
  });
}

async function createDataProperty(page: Page, parent: string, name: string): Promise<void> {
  await page.locator(ProjectView.tab('Data Properties')).click();
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
  await page.locator('button[title="Create"], button:has-text("Create")').first().click();
  await page.locator(CreateEntityDialog.name).fill(name);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(`text=${name}`).first()).toBeVisible({ timeout: 15_000 });
}

test('builds the Airplane ontology end-to-end', async ({ page, project }) => {
  test.setTimeout(180_000);

  // Sub-class hierarchy.
  await createSubClass(page, 'owl:Thing', 'Vehicle');
  await createSubClass(page, 'Vehicle', 'Aircraft');
  await createSubClass(page, 'Aircraft', 'FixedWingAircraft');
  await createSubClass(page, 'Aircraft', 'Helicopter');
  await createSubClass(page, 'FixedWingAircraft', 'CommercialAircraft');
  await createSubClass(page, 'FixedWingAircraft', 'MilitaryAircraft');
  await createSubClass(page, 'owl:Thing', 'Engine');
  await createSubClass(page, 'owl:Thing', 'Wing');
  await createSubClass(page, 'owl:Thing', 'FuselagePart');

  // Object properties.
  await createObjectProperty(page, 'owl:topObjectProperty', 'hasPart');
  await createObjectProperty(page, 'hasPart', 'hasEngine');
  await createObjectProperty(page, 'hasPart', 'hasWing');

  // Data properties.
  await createDataProperty(page, 'owl:topDataProperty', 'hasWeight');
  await createDataProperty(page, 'owl:topDataProperty', 'hasMaxAltitude');

  // Individuals.
  await createIndividual(page, 'Boeing747');
  await createIndividual(page, 'Boeing747_Engine_1');
  await createIndividual(page, 'Boeing747_Wing_1');
  await createIndividual(page, 'AirbusA320');
  await createIndividual(page, 'F16');
  await createIndividual(page, 'ApacheHelicopter');

  // Sanity: switch to History and verify several revisions exist.
  await page.locator(ProjectView.tab('History')).click();
  const yearStamps = page.locator(`text=/${new Date().getFullYear()}/`);
  await expect.poll(async () => yearStamps.count(), {
    timeout: 30_000,
  }).toBeGreaterThan(10);

  // Reload and confirm the hierarchy survives.
  await page.reload();
  await page.locator(ProjectView.tab('Classes')).click();
  for (const label of ['Vehicle', 'Aircraft', 'FixedWingAircraft', 'Helicopter']) {
    await expect(page.locator(Hierarchy.treeNode(label))).toBeVisible();
  }
});
