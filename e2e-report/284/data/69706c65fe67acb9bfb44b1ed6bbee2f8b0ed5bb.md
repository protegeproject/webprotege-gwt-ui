# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: 04-object-properties.spec.ts >> object properties >> OP6: drag-and-drop reparents an object property
- Location: tests/04-object-properties.spec.ts:90:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('.gt-tree__row:has(:text-is("opBeta"))')
Expected: visible
Error: strict mode violation: locator('.gt-tree__row:has(:text-is("opBeta"))') resolved to 2 elements:
    1) <div draggable="true" class="gt-tree__row GAW1CN3DF0B">…</div> aka locator('div').filter({ hasText: /^opBeta$/ }).first()
    2) <div draggable="true" class="gt-tree__row GAW1CN3DF0B">…</div> aka locator('div').filter({ hasText: /^opBeta$/ }).nth(4)

Call log:
  - Expect "toBeVisible" with timeout 15000ms
  - waiting for locator('.gt-tree__row:has(:text-is("opBeta"))')

```

# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - iframe
  - iframe
  - iframe
  - generic [ref=e7]:
    - generic [ref=e10]:
      - generic [ref=e11]: Projects
      - button "e2e@test.local ▾" [ref=e14] [cursor=pointer]
    - generic [ref=e15]:
      - generic [ref=e16]:
        - button "Create New Project" [ref=e17] [cursor=pointer]
        - generic [ref=e19]:
          - generic [ref=e21]:
            - checkbox "Owned by Me" [checked] [ref=e22]
            - text: Owned by Me
          - generic [ref=e24]:
            - checkbox "Shared with Me" [checked] [ref=e25]
            - text: Shared with Me
          - generic [ref=e27]:
            - checkbox "Trash" [ref=e28]
            - text: Trash
        - combobox [ref=e29]:
          - option "Sort by Last Opened" [selected]
          - option "Sort by Last Modified"
          - option "Sort by Project Name"
          - option "Sort by Owner"
      - generic [ref=e31]:
        - generic [ref=e32]: Project name
        - generic [ref=e33]: Owner
        - generic [ref=e34]: Last opened
        - generic [ref=e35]: Last modified
```

# Test source

```ts
  13  |       timeout: 15_000,
  14  |     });
  15  |   });
  16  | 
  17  |   test('OP1: switch to Object Properties shows the topObjectProperty root', async ({
  18  |     page,
  19  |   }) => {
  20  |     await expect(page.locator(Hierarchy.treeNode('owl:topObjectProperty'))).toBeVisible();
  21  |   });
  22  | 
  23  |   test('OP2: create object property hasPart', async ({ page }) => {
  24  |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  25  |     await page.locator(Hierarchy.toolbar.create).first().click();
  26  |     await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  27  |     await page.locator(CreateEntityDialog.name).fill('hasPart');
  28  |     await page.locator(CreateEntityDialog.submit).click();
  29  |     await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible({
  30  |       timeout: 15_000,
  31  |     });
  32  |   });
  33  | 
  34  |   test('OP3: create sub-property', async ({ page }) => {
  35  |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  36  |     await page.locator(Hierarchy.toolbar.create).first().click();
  37  |     await page.locator(CreateEntityDialog.name).fill('hasPart');
  38  |     await page.locator(CreateEntityDialog.submit).click();
  39  |     await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible();
  40  | 
  41  |     await page.locator(Hierarchy.treeNode('hasPart')).click();
  42  |     await page.locator(Hierarchy.toolbar.create).first().click();
  43  |     await page.locator(CreateEntityDialog.name).fill('hasEngine');
  44  |     await page.locator(CreateEntityDialog.submit).click();
  45  |     await expect(page.locator(Hierarchy.treeNode('hasEngine'))).toBeVisible({
  46  |       timeout: 15_000,
  47  |     });
  48  |   });
  49  | 
  50  |   test('OP5: bulk-create multiple object properties from one dialog', async ({
  51  |     page,
  52  |   }) => {
  53  |     const names = ['hasPart', 'hasEngine', 'hasWing', 'hasFuselage'];
  54  |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  55  |     await page.locator(Hierarchy.toolbar.create).first().click();
  56  |     await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  57  |     await page.locator(CreateEntityDialog.name).fill(names.join('\n'));
  58  |     await page.locator(CreateEntityDialog.submit).click();
  59  |     for (const name of names) {
  60  |       await expect(page.locator(Hierarchy.treeNode(name))).toBeVisible({
  61  |         timeout: 15_000,
  62  |       });
  63  |     }
  64  |   });
  65  | 
  66  |   test('OP4: set Domain and Range on a property', async ({ page }) => {
  67  |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  68  |     await page.locator(Hierarchy.toolbar.create).first().click();
  69  |     await page.locator(CreateEntityDialog.name).fill('hasPart');
  70  |     await page.locator(CreateEntityDialog.submit).click();
  71  |     await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible({
  72  |       timeout: 15_000,
  73  |     });
  74  | 
  75  |     await addPrimitiveValue(page, 'Domain', 'owl:Thing');
  76  |     await addPrimitiveValue(page, 'Range', 'owl:Thing');
  77  | 
  78  |     const domainRow = page
  79  |       .locator(FrameEditor.section('Domain'))
  80  |       .locator(FrameEditor.row)
  81  |       .filter({ hasText: 'owl:Thing' });
  82  |     const rangeRow = page
  83  |       .locator(FrameEditor.section('Range'))
  84  |       .locator(FrameEditor.row)
  85  |       .filter({ hasText: 'owl:Thing' });
  86  |     await expect(domainRow).toHaveCount(1);
  87  |     await expect(rangeRow).toHaveCount(1);
  88  |   });
  89  | 
  90  |   test('OP6: drag-and-drop reparents an object property', async ({ page }) => {
  91  |     // graphtree fires standard HTML5 drag/drop events on `.gt-tree__row`.
  92  |     // Playwright's `dragTo` drives the dragstart/dragover/drop sequence.
  93  |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  94  |     await page.locator(Hierarchy.toolbar.create).first().click();
  95  |     await page.locator(CreateEntityDialog.name).fill('opAlpha\nopBeta');
  96  |     await page.locator(CreateEntityDialog.submit).click();
  97  |     await expect(page.locator(Hierarchy.treeNode('opAlpha'))).toBeVisible({
  98  |       timeout: 15_000,
  99  |     });
  100 |     await expect(page.locator(Hierarchy.treeNode('opBeta'))).toBeVisible();
  101 | 
  102 |     await page
  103 |       .locator(Hierarchy.treeNode('opBeta'))
  104 |       .first()
  105 |       .dragTo(page.locator(Hierarchy.treeNode('opAlpha')).first());
  106 |     // Drain the MoveHierarchyNode RPC so the server's
  107 |     // EntityHierarchyChangedEvent updates the live tree, then let
  108 |     // PropertyHierarchyPortletPresenter.setSelectionInTree run its
  109 |     // post-move `revealTreeNodesForKey(opBeta)` — that expands every
  110 |     // ancestor on the path, so opAlpha is open and opBeta is on
  111 |     // screen without a chevron click.
  112 |     await page.waitForLoadState('networkidle');
> 113 |     await expect(page.locator(Hierarchy.treeNode('opBeta'))).toBeVisible({
      |                                                              ^ Error: expect(locator).toBeVisible() failed
  114 |       timeout: 15_000,
  115 |     });
  116 |   });
  117 | 
  118 |   test('OP7: add multiple annotations with language tags to a property', async ({
  119 |     page,
  120 |   }) => {
  121 |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  122 |     await page.locator(Hierarchy.toolbar.create).first().click();
  123 |     await page.locator(CreateEntityDialog.name).fill('hasPart');
  124 |     await page.locator(CreateEntityDialog.submit).click();
  125 |     await expect(page.locator(Hierarchy.treeNode('hasPart'))).toBeVisible({
  126 |       timeout: 15_000,
  127 |     });
  128 | 
  129 |     await addPropertyValue(page, 'Annotations', 'rdfs:label', 'has part', 'en');
  130 |     await addPropertyValue(page, 'Annotations', 'rdfs:label', 'hat Teil', 'de');
  131 |     await addPropertyValue(page, 'Annotations', 'rdfs:comment', 'Mereological link');
  132 | 
  133 |     const annotations = page
  134 |       .locator(FrameEditor.section('Annotations'))
  135 |       .locator(FrameEditor.row);
  136 |     await expect(annotations.filter({ hasText: 'has part' })).toHaveCount(1);
  137 |     await expect(annotations.filter({ hasText: 'hat Teil' })).toHaveCount(1);
  138 |     await expect(annotations.filter({ hasText: 'Mereological link' })).toHaveCount(1);
  139 |     // Language tag is stored in the row's `<input>`, outside textContent.
  140 |     await expect(
  141 |       annotations.filter({ hasText: 'has part' }).locator('input.gwt-SuggestBox'),
  142 |     ).toHaveValue('en');
  143 |     await expect(
  144 |       annotations.filter({ hasText: 'hat Teil' }).locator('input.gwt-SuggestBox'),
  145 |     ).toHaveValue('de');
  146 |   });
  147 | 
  148 |   test('OP8: delete a property', async ({ page }) => {
  149 |     await page.locator(Hierarchy.treeNode('owl:topObjectProperty')).click();
  150 |     await page.locator(Hierarchy.toolbar.create).first().click();
  151 |     await page.locator(CreateEntityDialog.name).fill('temporaryProp');
  152 |     await page.locator(CreateEntityDialog.submit).click();
  153 |     await expect(page.locator(Hierarchy.treeNode('temporaryProp'))).toBeVisible();
  154 | 
  155 |     await page.locator(Hierarchy.treeNode('temporaryProp')).click();
  156 |     await page.locator(Hierarchy.toolbar.delete).first().click();
  157 |     const confirm = page.locator('role=button >> text=/OK|Delete|Yes/').first();
  158 |     if (await confirm.isVisible().catch(() => false)) await confirm.click();
  159 |     await expect(page.locator(Hierarchy.treeNode('temporaryProp'))).toHaveCount(0);
  160 |   });
  161 | });
  162 | 
```