# Playwright E2E tests for webprotege-gwt-ui

End-to-end tests for the WebProtege GWT UI. Tracked under
[issue #267](https://github.com/protegeproject/webprotege-gwt-ui/issues/267).

## Quick start

```bash
# 1. Install dependencies and browsers (one-time)
cd playwright
npm install
npx playwright install --with-deps chromium

# 2. Bring up the full webprotege stack
#    (Keycloak + MongoDB + RabbitMQ + MinIO + backend services + NGINX)
#    Default exposes the UI at http://localhost/ (NGINX on port 80).
#    On macOS port 80 needs sudo; copy .env.example to .env and set
#    WEBPROTEGE_HOST_PORT=8080 to use a non-privileged port instead.
npm run stack:up

# 3. Run the suite
npm test                  # headless
npm run test:headed       # watch it click
npm run test:smoke        # ~30s sanity scenario

# 4. Tear down when done
npm run stack:down
```

The compose stack uses the upstream `protegeproject/webprotege-keycloak` image,
which imports the `webprotege` realm on first boot. `globalSetup.ts` then
ensures one fixed test user exists in that realm (idempotent — safe to re-run):

| field | value |
|---|---|
| email | `e2e@test.local` |
| password | `testtest123` |

`globalSetup.ts` logs in once and reuses the resulting storage state across
specs.

## Layout

```
playwright/
├── docker-compose.test.yml   # full test stack (mirrors webprotege-deploy minus ELK)
├── .env.example              # SERVER_HOST, WEBPROTEGE_HOST_PORT, ADMIN_CLI_SECRET
├── fixtures/                 # sample .owl files for upload tests
├── support/                  # selectors, api helpers, per-test fixtures
├── tests/                    # atomic spec files (01-auth, 02-projects, ...)
└── tests/scenarios/          # multi-step scenarios (airplane, smoke)
```

## Configuration

| Env var | Default | What it does |
|---|---|---|
| `WEBPROTEGE_BASE_URL` | `http://localhost` | URL Playwright drives the browser at |
| `WEBPROTEGE_HOST_PORT` | `80` | Host port the NGINX edge container exposes |
| `SERVER_HOST` | `localhost` | Hostname Keycloak/services use for redirects |

## CI

The suite runs headless on every PR that touches `webprotege-gwt-ui-client`,
`webprotege-gwt-ui-shared`, or `playwright/**`. See
`.github/workflows/e2e.yaml`. On failure the workflow uploads
`playwright-report/` as an artifact — download and run
`npx playwright show-trace <trace.zip>` to replay the failure.
