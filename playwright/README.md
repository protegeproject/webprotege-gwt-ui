# Playwright E2E tests for webprotege-gwt-ui

End-to-end tests for the WebProtege GWT UI. Tracked under
[issue #267](https://github.com/protegeproject/webprotege-gwt-ui/issues/267).

## Quick start

```bash
# 1. Install dependencies and browsers (one-time)
cd playwright
npm install
npx playwright install --with-deps chromium

# 2. Configure the stack hostname (one-time)
#    cp .env.example .env
#    The realm import only registers `http://webprotege-local.edu/*` as a
#    valid redirect URI for the `webprotege` Keycloak client, so the stack
#    must be addressable under that host. Add a matching /etc/hosts entry:
#       127.0.0.1   webprotege-local.edu
#    Without this, Keycloak rejects the OAuth redirect with
#    "Invalid parameter: redirect_uri" and the login page never renders.

# 3. Bring up the full webprotege stack
#    (Keycloak + MongoDB + RabbitMQ + MinIO + backend services + NGINX)
#    Exposes the UI at http://webprotege-local.edu/ (NGINX on port 80).
#    On macOS port 80 needs sudo; set WEBPROTEGE_HOST_PORT=8080 in .env to
#    use a non-privileged port instead, and adjust WEBPROTEGE_BASE_URL
#    accordingly when invoking Playwright.
npm run stack:up

# 4. Run the suite (point Playwright at the same host as the stack)
WEBPROTEGE_BASE_URL=http://webprotege-local.edu npm test            # headless
WEBPROTEGE_BASE_URL=http://webprotege-local.edu npm run test:headed # watch it click
WEBPROTEGE_BASE_URL=http://webprotege-local.edu npm run test:smoke  # ~30s sanity scenario

# 5. Tear down when done
npm run stack:down
```

## Stack images and run modes

The stack pulls each dependency service (Keycloak, backend-service, gateway,
event-history, …) at an **explicit pinned version** matching the set that
[`webprotege-deploy`](https://github.com/protegeproject/webprotege-deploy)
runs. Versions are pinned rather than floating on `:latest` because (a) these
images do not publish a `:latest` tag, and (b) a per-PR e2e run must be
reproducible — a moving `:latest` would let unrelated upstream changes turn CI
red. When bumping, mirror `webprotege-deploy/docker-compose.yml`. Do not use
`-WHO` fork tags.

Only `webprotege-gwt-ui-server` is built locally (`:local`, via the `build:`
in `docker-compose.yml`) — it is the code under test, so `npm run stack:up`
already rebuilds it from `../webprotege-gwt-ui-server`. Pass `--build` (the
`stack:up` script does not) after changing UI code, or use the dev mode below.

**Default mode** — local UI + pinned published dependencies (what CI uses):

```bash
npm run stack:up      # docker compose up -d --wait
```

**Dev mode** — also run *locally-built* dependency services. Use this when you
are modifying, say, `webprotege-backend-service` and want the stack to run your
build of it. It layers `docker-compose.dev.yml` (opt-in via `-f`, so it never
silently forces a stale local image the way an auto-loaded
`docker-compose.override.yml` would):

```bash
npm run stack:up:dev    # -f docker-compose.yml -f docker-compose.dev.yml up --build
npm run stack:down:dev
```

The dependency sibling repos (e.g. `../../webprotege-backend-service`) must be
checked out next to `webprotege-gwt-ui`. Add more services to
`docker-compose.dev.yml` as needed.

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
├── docker-compose.yml        # full test stack (mirrors webprotege-deploy minus ELK)
├── docker-compose.dev.yml    # opt-in (-f) overrides to run locally-built dependency services
├── .env.example              # SERVER_HOST, WEBPROTEGE_HOST_PORT, ADMIN_CLI_SECRET
├── fixtures/                 # sample .owl files for upload tests
├── support/                  # selectors, api helpers, per-test fixtures
├── tests/                    # atomic spec files (01-auth, 02-projects, ...)
└── tests/scenarios/          # multi-step scenarios (airplane, smoke)
```

## Configuration

| Env var | Default | What it does |
|---|---|---|
| `WEBPROTEGE_BASE_URL` | `http://localhost` | URL Playwright drives the browser at — set to `http://webprotege-local.edu` so it matches `SERVER_HOST` |
| `WEBPROTEGE_HOST_PORT` | `80` | Host port the NGINX edge container exposes |
| `SERVER_HOST` | `localhost` | Hostname Keycloak/services use for redirects — must be `webprotege-local.edu` to match the realm's registered redirect URIs |

## CI

The suite runs headless on every PR that touches `webprotege-gwt-ui-client`,
`webprotege-gwt-ui-shared`, or `playwright/**`. See
`.github/workflows/e2e.yaml`. On failure the workflow uploads
`playwright-report/` as an artifact — download and run
`npx playwright show-trace <trace.zip>` to replay the failure.
