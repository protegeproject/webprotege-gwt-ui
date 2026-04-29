# Playwright E2E tests for webprotege-gwt-ui

End-to-end tests for the WebProtege GWT UI. Tracked under
[issue #267](https://github.com/protegeproject/webprotege-gwt-ui/issues/267).

## Quick start

```bash
# 1. Bring up Keycloak + MongoDB + webprotege backend + UI
npm run stack:up

# 2. Install browsers (one-time)
npm install
npx playwright install --with-deps chromium

# 3. Run the suite
npm test                  # headless
npm run test:headed       # watch it click
npm run test:smoke        # ~30s sanity scenario

# 4. Tear down when done
npm run stack:down
```

The compose stack pre-imports a Keycloak realm with one fixed test user:

| field | value |
|---|---|
| email | `e2e@test.local` |
| password | `testtest123` |

`globalSetup.ts` logs in once and reuses the resulting storage state across
specs.

## Layout

```
playwright/
├── docker-compose.test.yml   # full test stack
├── fixtures/                 # realm export, sample .owl files
├── support/                  # selectors, api helpers, per-test fixtures
├── tests/                    # atomic spec files (01-auth, 02-projects, ...)
└── tests/scenarios/          # multi-step scenarios (airplane, smoke)
```

## CI

The suite runs headless on every PR that touches `webprotege-gwt-ui-client`,
`webprotege-gwt-ui-shared`, or `playwright/**`. See
`.github/workflows/e2e.yaml`. On failure the workflow uploads
`playwright-report/` as an artifact — download and run
`npx playwright show-trace <trace.zip>` to replay the failure.
