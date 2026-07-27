import * as fs from 'fs';
import * as path from 'path';
import { Page } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import { CreateEntityDialog, Hierarchy, ProjectView } from '../support/selectors';

/**
 * UI performance baseline for epic #303 (WebSocket/STOMP -> SSE).
 *
 * Three user-perceived metrics, measured against whatever stack is running:
 *   1. Acting-user latency: create-class dialog submit -> new row in own tree.
 *   2. Propagation delay: user A submits -> the row appears in user B's tree.
 *   3. Project-open time, plus the size/duration of the project-events
 *      history request that the 10s polling timer fires after open (#301).
 *
 * Results are printed, attached to the HTML report, and written as JSON to
 * test-results/perf/ so before/after runs can be diffed. Run via
 * `npm run test:perf` (workers=1, retries=0 — see playwright.perf.config.ts).
 *
 * Note for before/after comparisons: the project fixture installs a
 * MutationObserver error gate on the page. Its overhead is small and constant
 * and it is present in every run using this harness, so comparisons remain
 * valid — but do not compare against numbers gathered without the fixture.
 */

const STORAGE_STATE = path.join(__dirname, '..', '.auth', 'storageState.json');
// Not under test-results/ — Playwright wipes that directory at the start of
// every run, which would destroy earlier metrics when re-running one test.
const RESULTS_DIR = path.join(__dirname, '..', 'perf-results');

const LATENCY_ITERATIONS = 15;
const PROPAGATION_ITERATIONS = 12;
const OPEN_ITERATIONS = 5;
const SEED_CLASS_COUNT = 25;
// The first poll fires one full polling period (~10s) after project open, so
// each open-iteration parks this long to catch the history request.
const POLL_CAPTURE_WINDOW_MS = 13_000;

test.describe.configure({ mode: 'serial' });

interface MetricStats {
  n: number;
  medianMs: number;
  p90Ms: number;
  minMs: number;
  maxMs: number;
  samplesMs: number[];
}

function statsOf(samples: number[]): MetricStats {
  const sorted = [...samples].sort((a, b) => a - b);
  const at = (q: number) => sorted[Math.min(sorted.length - 1, Math.floor(q * sorted.length))];
  return {
    n: sorted.length,
    medianMs: Math.round(at(0.5)),
    p90Ms: Math.round(at(0.9)),
    minMs: Math.round(sorted[0]),
    maxMs: Math.round(sorted[sorted.length - 1]),
    samplesMs: samples.map((s) => Math.round(s)),
  };
}

function saveResults(name: string, data: unknown): void {
  fs.mkdirSync(RESULTS_DIR, { recursive: true });
  fs.writeFileSync(path.join(RESULTS_DIR, `${name}.json`), JSON.stringify(data, null, 2));
}

function runMetadata() {
  return {
    baseUrl: process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost',
    capturedAt: new Date().toISOString(),
  };
}

/** Same 12 lines as tests/03-classes.spec.ts — duplicated per suite precedent. */
async function createClassUnder(page: Page, parentLabel: string, newLabel: string): Promise<void> {
  await page.locator(Hierarchy.treeNode(parentLabel)).first().click();
  await page.locator(Hierarchy.toolbar.create).first().click();
  await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
  await page.locator(CreateEntityDialog.name).fill(newLabel);
  await page.locator(CreateEntityDialog.submit).click();
  await expect(page.locator(Hierarchy.treeNode(newLabel))).toBeVisible({
    timeout: 15_000,
  });
}

/**
 * Arm in-page timestamps: t0 on the dialog's primary-button click, t1 when a
 * tree row containing `label` first appears. Both are epoch-based
 * (performance.timeOrigin + performance.now()) so they can be compared across
 * pages on the same machine. In-page measurement avoids Playwright RPC/poll
 * jitter, which is comparable in size to sub-second latencies.
 */
async function armSubmitProbe(page: Page, label: string): Promise<void> {
  await page.evaluate((newLabel) => {
    const w = window as any;
    w.__perf = { t0: 0, t1: 0 };
    if (w.__perfClick) document.removeEventListener('click', w.__perfClick, true);
    w.__perfClick = (e: Event) => {
      const target = e.target as HTMLElement | null;
      if (w.__perf.t0 === 0 && target?.closest('.wp-modal button.wp-btn--dialog.wp-btn--primary')) {
        w.__perf.t0 = performance.timeOrigin + performance.now();
      }
    };
    document.addEventListener('click', w.__perfClick, true);
    if (w.__perfObs) w.__perfObs.disconnect();
    w.__perfObs = new MutationObserver(() => {
      for (const row of document.querySelectorAll('.gt-tree__row')) {
        if (row.textContent && row.textContent.includes(newLabel)) {
          w.__perf.t1 = performance.timeOrigin + performance.now();
          w.__perfObs.disconnect();
          return;
        }
      }
    });
    w.__perfObs.observe(document.body, { childList: true, subtree: true });
  }, label);
}

/** Arm only the appearance half of the probe (for the observing page B). */
async function armAppearanceProbe(page: Page, label: string): Promise<void> {
  await page.evaluate((newLabel) => {
    const w = window as any;
    w.__perf = { t0: 0, t1: 0 };
    if (w.__perfObs) w.__perfObs.disconnect();
    w.__perfObs = new MutationObserver(() => {
      for (const row of document.querySelectorAll('.gt-tree__row')) {
        if (row.textContent && row.textContent.includes(newLabel)) {
          w.__perf.t1 = performance.timeOrigin + performance.now();
          w.__perfObs.disconnect();
          return;
        }
      }
    });
    w.__perfObs.observe(document.body, { childList: true, subtree: true });
  }, label);
}

const readPerf = (page: Page) => page.evaluate(() => (window as any).__perf as { t0: number; t1: number });

test('P1: acting-user latency — create-class submit to own tree update', async ({ page, project }, testInfo) => {
  const samples: number[] = [];
  for (let i = 0; i < LATENCY_ITERATIONS; i++) {
    const label = `PerfLat_${i}`;
    // Open and fill the dialog BEFORE arming, so the only primary-button
    // click after arming is the submit we want to time.
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill(label);
    await armSubmitProbe(page, label);
    await page.locator(CreateEntityDialog.submit).click();
    await page.waitForFunction(() => (window as any).__perf.t1 > 0, undefined, { timeout: 20_000 });
    const { t0, t1 } = await readPerf(page);
    expect(t0, 'submit click was not captured').toBeGreaterThan(0);
    samples.push(t1 - t0);
  }
  const result = { metric: 'acting-user-latency', ...runMetadata(), stats: statsOf(samples) };
  console.log(JSON.stringify(result, null, 2));
  await testInfo.attach('acting-user-latency', { body: JSON.stringify(result, null, 2), contentType: 'application/json' });
  saveResults('acting-user-latency', result);
});

test('P2: propagation delay — user A edit to user B tree update', async ({ page, browser, project }, testInfo) => {
  // Second session as the same user, reusing the saved storage state — both
  // sessions receive project events; no sharing setup needed.
  const contextB = await browser.newContext({ storageState: STORAGE_STATE });
  const pageB = await contextB.newPage();

  // Transport diagnostics on B: websocket frames on /wsapps, and completed
  // GetProjectEventsAction polls. Registered before goto so the socket opened
  // during bootstrap is captured. Node Date.now() is comparable to the pages'
  // epoch stamps on the same machine to within ms.
  const wsFrameTimes: number[] = [];
  pageB.on('websocket', (ws) => {
    if (!/wsapps/.test(ws.url())) return;
    ws.on('framereceived', () => wsFrameTimes.push(Date.now()));
  });
  // GWT-RPC bodies are obfuscated in production compiles, so individual
  // actions can't be identified; count every dispatch round-trip instead.
  // On the passive page B these are only the 10s poll ticks and the
  // TranslateEventListAction round-trip a websocket frame triggers.
  const pollDoneTimes: number[] = [];
  pageB.on('response', (response) => {
    if (!/dispatchservice/i.test(response.request().url())) return;
    pollDoneTimes.push(Date.now());
  });

  await pageB.goto(project.url);
  await expect(pageB.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
  await expect(pageB.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({ timeout: 15_000 });

  // Warmup edit: proves B's tree is live (and reveals owl:Thing's children)
  // before measurement starts. Falls back to selecting the root if the row
  // does not surface on its own.
  await armAppearanceProbe(pageB, 'PerfProp_warm');
  await createClassUnder(page, 'owl:Thing', 'PerfProp_warm');
  try {
    await pageB.waitForFunction(() => (window as any).__perf.t1 > 0, undefined, { timeout: 30_000 });
  } catch {
    await pageB.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await pageB.waitForFunction(() => (window as any).__perf.t1 > 0, undefined, { timeout: 15_000 });
  }

  const samples: Array<{ deltaMs: number; wsFramesInWindow: number; dispatchRoundTripsInWindow: number }> = [];
  for (let i = 0; i < PROPAGATION_ITERATIONS; i++) {
    const label = `PerfProp_${i}`;
    await armAppearanceProbe(pageB, label);
    await page.locator(Hierarchy.treeNode('owl:Thing')).first().click();
    await page.locator(Hierarchy.toolbar.create).first().click();
    await expect(page.locator(CreateEntityDialog.root)).toBeVisible();
    await page.locator(CreateEntityDialog.name).fill(label);
    await armSubmitProbe(page, label);
    await page.locator(CreateEntityDialog.submit).click();
    await expect(page.locator(Hierarchy.treeNode(label))).toBeVisible({ timeout: 15_000 });
    // Worst case today is the 10s polling safety net; allow head-room.
    await pageB.waitForFunction(() => (window as any).__perf.t1 > 0, undefined, { timeout: 30_000 });
    const { t0 } = await readPerf(page);
    const { t1 } = await readPerf(pageB);
    expect(t0, 'submit click was not captured on A').toBeGreaterThan(0);
    samples.push({
      deltaMs: t1 - t0,
      wsFramesInWindow: wsFrameTimes.filter((t) => t >= t0 - 50 && t <= t1 + 50).length,
      dispatchRoundTripsInWindow: pollDoneTimes.filter((t) => t >= t0 - 50 && t <= t1 + 50).length,
    });
  }
  await contextB.close();

  const result = {
    metric: 'propagation-delay',
    ...runMetadata(),
    stats: statsOf(samples.map((s) => s.deltaMs)),
    // Propagation under STOMP+polling is expected to be bimodal; the raw
    // per-sample transport diagnostics make the split interpretable.
    samples,
  };
  console.log(JSON.stringify(result, null, 2));
  await testInfo.attach('propagation-delay', { body: JSON.stringify(result, null, 2), contentType: 'application/json' });
  saveResults('propagation-delay', result);
});

test('P3: project-open time and event-history request', async ({ page, project }, testInfo) => {
  // Seed history with sequential creates — each is its own revision/event
  // batch (a single bulk create would collapse into one revision).
  for (let i = 0; i < SEED_CLASS_COUNT; i++) {
    await createClassUnder(page, 'owl:Thing', `PerfSeed_${String(i).padStart(2, '0')}`);
  }

  // The production GWT compile obfuscates RPC type names (verified: request
  // bodies carry hash tokens like "8a", never "GetProjectEventsAction"), so
  // requests cannot be identified by body content. Instead, capture every
  // dispatchservice round-trip: the page is parked idle after ready, so any
  // request initiated during the park window is timer-driven — i.e. the
  // project-events poll. The full-history response is the largest of those.
  interface EventsRequestSample {
    sinceNavMs: number;
    requestMs: number;
    bytes: number;
  }
  let navStart = 0;
  let dispatchSamples: EventsRequestSample[] = [];
  page.on('response', async (response) => {
    const req = response.request();
    if (!/dispatchservice/i.test(req.url())) return;
    try {
      dispatchSamples.push({
        sinceNavMs: Date.now() - navStart,
        requestMs: Math.round(response.request().timing().responseEnd),
        bytes: (await response.body()).byteLength,
      });
    } catch {
      // Body unavailable after navigation — skip the sample.
    }
  });

  const opens: Array<{ kind: 'warmup' | 'measured'; readyMs: number; historyRequest?: EventsRequestSample; parkedRequests?: EventsRequestSample[] }> = [];
  for (let i = 0; i <= OPEN_ITERATIONS; i++) {
    dispatchSamples = [];
    // about:blank first forces a genuine GWT re-bootstrap without a Keycloak
    // round-trip (a hard reload can bounce through Keycloak and drop the
    // deep-link hash).
    await page.goto('about:blank');
    navStart = Date.now();
    await page.goto(project.url);
    await expect(page.locator(ProjectView.root)).toBeVisible({ timeout: 30_000 });
    await expect(page.locator(Hierarchy.treeNode('owl:Thing'))).toBeVisible({ timeout: 15_000 });
    const readyMs = Date.now() - navStart;
    expect(page.url(), 'landed off the project (Keycloak bounce?)').toContain(project.url.split('#')[1].split('/')[1]);
    // Park to catch the polling timer's first (full-history) request, which
    // fires one full period after open — not at open.
    await page.waitForTimeout(POLL_CAPTURE_WINDOW_MS);
    // Poll ticks are the requests initiated while parked (well after ready).
    const parked = dispatchSamples.filter((s) => s.sinceNavMs > readyMs + 2_000);
    const history = [...parked].sort((a, b) => b.bytes - a.bytes)[0];
    opens.push({
      kind: i === 0 ? 'warmup' : 'measured',
      readyMs,
      historyRequest: history,
      parkedRequests: parked,
    });
  }

  const measured = opens.filter((o) => o.kind === 'measured');
  expect(
    measured.some((o) => o.historyRequest),
    'no GetProjectEventsAction request captured — postData filter or park window is wrong',
  ).toBeTruthy();
  const result = {
    metric: 'project-open',
    ...runMetadata(),
    seedClassCount: SEED_CLASS_COUNT,
    readyStats: statsOf(measured.map((o) => o.readyMs)),
    historyRequestBytes: statsOf(measured.filter((o) => o.historyRequest).map((o) => o.historyRequest!.bytes)),
    historyRequestMs: statsOf(measured.filter((o) => o.historyRequest).map((o) => o.historyRequest!.requestMs)),
    opens,
  };
  console.log(JSON.stringify(result, null, 2));
  await testInfo.attach('project-open', { body: JSON.stringify(result, null, 2), contentType: 'application/json' });
  saveResults('project-open', result);
});
