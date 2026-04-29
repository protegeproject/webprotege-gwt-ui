import { APIRequestContext, request } from '@playwright/test';
import { getUserAccessToken, KEYCLOAK_DEFAULTS } from './keycloak';

/**
 * Helpers that talk to the WebProtege GWT-RPC dispatch service directly,
 * for setup/teardown speed when a test does not need to drive the UI.
 *
 * Endpoint: POST {baseURL}/webprotege/dispatchservice
 * Auth: Bearer access_token from Keycloak (Direct Access Grant).
 *
 * The dispatch service expects a serialised `Action` payload — the exact
 * JSON shape is defined by individual `*Action` classes in
 * webprotege-gwt-ui-shared. Helpers below wrap the actions tests reach
 * for most: project create / trash, and entity bulk create.
 *
 * Implementation note: GWT-RPC over JSON requires a `ChangeRequestId`
 * for any action that mutates an ontology. Helpers generate one.
 */

const BASE_URL = process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost';

let cachedToken: { value: string; expiresAt: number } | null = null;

async function getToken(): Promise<string> {
  const now = Date.now();
  if (cachedToken && cachedToken.expiresAt > now + 30_000) return cachedToken.value;
  const token = await getUserAccessToken(KEYCLOAK_DEFAULTS);
  cachedToken = { value: token, expiresAt: now + 4 * 60_000 };
  return token;
}

export async function authedRequest(): Promise<APIRequestContext> {
  const token = await getToken();
  return request.newContext({
    baseURL: BASE_URL,
    extraHTTPHeaders: { Authorization: `Bearer ${token}` },
  });
}

export function newChangeRequestId(): string {
  // UUID v4 — matches what the webprotege client generates.
  // crypto.randomUUID is available on Node 19+ which Playwright requires.
  return crypto.randomUUID();
}
