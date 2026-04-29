import { request } from '@playwright/test';

export interface KeycloakConfig {
  /** Base URL of the WebProtege deployment, e.g. http://localhost. */
  baseUrl: string;
  /** Realm under which webprotege users live. Always 'webprotege'. */
  realm: string;
  /** Master-realm admin credentials for kcadm/REST admin operations. */
  adminUser: string;
  adminPassword: string;
}

export interface TestUser {
  email: string;
  username: string;
  password: string;
  firstName?: string;
  lastName?: string;
}

const ADMIN_TOKEN_PATH = '/keycloak/realms/master/protocol/openid-connect/token';
const REALM_TOKEN_PATH = (realm: string) =>
  `/keycloak/realms/${realm}/protocol/openid-connect/token`;
const ADMIN_USERS_PATH = (realm: string) => `/keycloak/admin/realms/${realm}/users`;

export const TEST_USER: TestUser = {
  email: 'e2e@test.local',
  username: 'e2e@test.local',
  password: 'testtest123',
  firstName: 'E2E',
  lastName: 'Tester',
};

export const KEYCLOAK_DEFAULTS: KeycloakConfig = {
  baseUrl: process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost',
  realm: 'webprotege',
  adminUser: process.env.KEYCLOAK_ADMIN ?? 'admin',
  adminPassword: process.env.KEYCLOAK_ADMIN_PASSWORD ?? 'password',
};

export async function getAdminToken(cfg: KeycloakConfig): Promise<string> {
  const ctx = await request.newContext({ baseURL: cfg.baseUrl });
  try {
    const res = await ctx.post(ADMIN_TOKEN_PATH, {
      form: {
        grant_type: 'password',
        client_id: 'admin-cli',
        username: cfg.adminUser,
        password: cfg.adminPassword,
      },
    });
    if (!res.ok()) {
      throw new Error(`Keycloak admin token failed: ${res.status()} ${await res.text()}`);
    }
    const body = await res.json();
    return body.access_token as string;
  } finally {
    await ctx.dispose();
  }
}

export async function ensureTestUser(
  cfg: KeycloakConfig,
  user: TestUser = TEST_USER,
): Promise<void> {
  const adminToken = await getAdminToken(cfg);
  const ctx = await request.newContext({
    baseURL: cfg.baseUrl,
    extraHTTPHeaders: { Authorization: `Bearer ${adminToken}` },
  });
  try {
    const lookup = await ctx.get(ADMIN_USERS_PATH(cfg.realm), {
      params: { username: user.username, exact: 'true' },
    });
    if (!lookup.ok()) {
      throw new Error(`Lookup failed: ${lookup.status()} ${await lookup.text()}`);
    }
    const existing = (await lookup.json()) as Array<{ id: string }>;
    if (existing.length > 0) return;

    const create = await ctx.post(ADMIN_USERS_PATH(cfg.realm), {
      data: {
        username: user.username,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        emailVerified: true,
        enabled: true,
        attributes: { webprotege_username: [user.username] },
        credentials: [
          { type: 'password', value: user.password, temporary: false },
        ],
      },
    });
    if (!create.ok() && create.status() !== 409) {
      throw new Error(`Create user failed: ${create.status()} ${await create.text()}`);
    }
  } finally {
    await ctx.dispose();
  }
}

/**
 * Direct Access Grant — fetches an access token for a normal realm user.
 * Used by fixtures that need to drive backend APIs directly to set up or
 * clean up test data without going through the UI.
 */
export async function getUserAccessToken(
  cfg: KeycloakConfig,
  user: TestUser = TEST_USER,
): Promise<string> {
  const ctx = await request.newContext({ baseURL: cfg.baseUrl });
  try {
    const res = await ctx.post(REALM_TOKEN_PATH(cfg.realm), {
      form: {
        grant_type: 'password',
        client_id: 'webprotege',
        username: user.username,
        password: user.password,
        scope: 'openid',
      },
    });
    if (!res.ok()) {
      throw new Error(`User token failed: ${res.status()} ${await res.text()}`);
    }
    const body = await res.json();
    return body.access_token as string;
  } finally {
    await ctx.dispose();
  }
}
