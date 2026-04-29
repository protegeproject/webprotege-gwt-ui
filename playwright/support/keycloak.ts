import { APIRequestContext, request } from '@playwright/test';

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
const ADMIN_CLIENTS_PATH = (realm: string) => `/keycloak/admin/realms/${realm}/clients`;

/**
 * Tests need both webprotege client roles. The realm seed creates
 * `SystemAdmin` but `BuiltInRole.SYSTEM_ADMIN` does NOT transitively include
 * `CREATE_EMPTY_PROJECT` — that lives on `PROJECT_CREATOR`, which the realm
 * seed does not create. So globalSetup ensures the `ProjectCreator` client
 * role exists (matching the UPPER_UNDERSCORE→UPPER_CAMEL convention from
 * BuiltInRole.java) and assigns it alongside `SystemAdmin` to the test user.
 */
const WEBPROTEGE_CLIENT_ID = 'webprotege';
const REQUIRED_CLIENT_ROLES = ['SystemAdmin', 'ProjectCreator'] as const;

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
    let userId: string;
    if (existing.length > 0) {
      userId = existing[0].id;
    } else {
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
      const reLookup = await ctx.get(ADMIN_USERS_PATH(cfg.realm), {
        params: { username: user.username, exact: 'true' },
      });
      const created = (await reLookup.json()) as Array<{ id: string }>;
      if (created.length === 0) {
        throw new Error(`Created user ${user.username} not found on re-lookup`);
      }
      userId = created[0].id;
    }
    await ensureRequiredClientRoles(ctx, cfg.realm, userId);
  } finally {
    await ctx.dispose();
  }
}

async function ensureRequiredClientRoles(
  ctx: APIRequestContext,
  realm: string,
  userId: string,
): Promise<void> {
  const clientLookup = await ctx.get(ADMIN_CLIENTS_PATH(realm), {
    params: { clientId: WEBPROTEGE_CLIENT_ID },
  });
  if (!clientLookup.ok()) {
    throw new Error(`Client lookup failed: ${clientLookup.status()} ${await clientLookup.text()}`);
  }
  const clients = (await clientLookup.json()) as Array<{ id: string }>;
  if (clients.length === 0) {
    throw new Error(`Keycloak client '${WEBPROTEGE_CLIENT_ID}' not found in realm '${realm}'`);
  }
  const clientUuid = clients[0].id;
  const rolesPath = `${ADMIN_CLIENTS_PATH(realm)}/${clientUuid}/roles`;
  const mappingPath = `${ADMIN_USERS_PATH(realm)}/${userId}/role-mappings/clients/${clientUuid}`;

  const currentMapping = await ctx.get(mappingPath);
  const currentlyAssigned = currentMapping.ok()
    ? ((await currentMapping.json()) as Array<{ name: string }>).map((r) => r.name)
    : [];

  const toAssign: Array<{ id: string; name: string }> = [];
  for (const roleName of REQUIRED_CLIENT_ROLES) {
    if (currentlyAssigned.includes(roleName)) continue;
    let roleLookup = await ctx.get(`${rolesPath}/${roleName}`);
    if (roleLookup.status() === 404) {
      const create = await ctx.post(rolesPath, { data: { name: roleName } });
      if (!create.ok() && create.status() !== 409) {
        throw new Error(`Create role ${roleName} failed: ${create.status()} ${await create.text()}`);
      }
      roleLookup = await ctx.get(`${rolesPath}/${roleName}`);
    }
    if (!roleLookup.ok()) {
      throw new Error(`Role lookup ${roleName} failed: ${roleLookup.status()} ${await roleLookup.text()}`);
    }
    const role = (await roleLookup.json()) as { id: string; name: string };
    toAssign.push({ id: role.id, name: role.name });
  }
  if (toAssign.length === 0) return;

  const assign = await ctx.post(mappingPath, { data: toAssign });
  if (!assign.ok() && assign.status() !== 204) {
    throw new Error(`Assign roles failed: ${assign.status()} ${await assign.text()}`);
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
