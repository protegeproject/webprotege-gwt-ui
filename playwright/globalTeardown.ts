import { execFileSync } from 'child_process';

/**
 * Runs once after the entire suite finishes, regardless of pass/fail.
 *
 * Per-test cleanup in support/fixtures.ts trashes each project the test
 * opted into via the `project` fixture, but it can't catch tests that
 * crash before fixture teardown, tests that create projects outside the
 * fixture, or trash dispatches that quietly fail. Without a backstop the
 * project list balloons over time and slows the next run's create-row
 * waits enough to flake.
 *
 * Strategy: shell out to mongosh inside the local docker stack and drop
 * every project whose displayName matches the e2e naming convention
 * (`Test_…` / `RoundTrip_…` / `Dbg_…`). Skipped automatically when the
 * mongo container isn't reachable, so this is safe in CI / on machines
 * without the dev stack running.
 */
async function globalTeardown(): Promise<void> {
  if (process.env.WEBPROTEGE_E2E_SKIP_TEARDOWN === '1') return;

  const container = process.env.WEBPROTEGE_MONGO_CONTAINER ?? 'playwright-mongo-1';
  const script = `
    const ids = db.ProjectDetails
      .find({displayName: {$regex: "^(Test|RoundTrip|Dbg)_"}}, {_id: 1})
      .toArray().map(p => p._id);
    if (ids.length === 0) { print("[teardown] no test projects to purge"); quit(); }
    const collections = ["ProjectAccess","RoleAssignments","PerspectiveLayouts",
                         "EntityCrudKitSettings","EntityTags","Forms",
                         "ProjectOrderedChildren","UserActivity"];
    let removed = 0;
    collections.forEach(c => { removed += db.getCollection(c).deleteMany({projectId: {$in: ids}}).deletedCount; });
    removed += db.ProjectDetails.deleteMany({_id: {$in: ids}}).deletedCount;
    print("[teardown] purged " + ids.length + " projects, " + removed + " rows total");
  `;

  try {
    const out = execFileSync(
      'docker',
      ['exec', container, 'mongosh', 'webprotege', '--quiet', '--eval', script],
      { stdio: ['ignore', 'pipe', 'pipe'], timeout: 30_000 },
    );
    process.stdout.write(out);
  } catch (err) {
    // Best-effort: if docker / the mongo container isn't around (e.g. in
    // CI before stack is wired in), just log and move on.
    console.warn('[teardown] project purge skipped:', (err as Error).message);
  }
}

export default globalTeardown;
