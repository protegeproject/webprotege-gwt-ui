import { request } from '@playwright/test';
import { test, expect, projectIdOf } from '../support/fixtures';
import {
  COLLAB_USER,
  KEYCLOAK_DEFAULTS,
  TEST_USER,
  ensureTestUser,
  getUserAccessToken,
} from '../support/keycloak';

/**
 * Authorization coverage for the SSE stream (#305) and, by the same capability
 * check, the events pull path (#295): a user without view permission on a
 * project must not be able to obtain a stream ticket for it, and the stream
 * itself must refuse connections that do not carry a live ticket.
 */

const BASE_URL = process.env.WEBPROTEGE_BASE_URL ?? 'http://localhost';
const TICKET_PATH = '/data/events/ticket';

test.describe('SSE stream authorization', () => {
  test.beforeAll(async () => {
    await ensureTestUser(KEYCLOAK_DEFAULTS, COLLAB_USER, []);
  });

  test('SEC1: no view permission means no stream ticket', async ({ project }) => {
    const collabToken = await getUserAccessToken(KEYCLOAK_DEFAULTS, COLLAB_USER);
    const api = await request.newContext({
      baseURL: BASE_URL,
      extraHTTPHeaders: { Authorization: `Bearer ${collabToken}` },
    });
    const res = await api.post(TICKET_PATH, { data: { projectId: projectIdOf(project) } });
    expect(res.status()).toBe(403);
    await api.dispose();
  });

  test('SEC2: the owner is issued a short-lived single-project ticket', async ({ project }) => {
    const ownerToken = await getUserAccessToken(KEYCLOAK_DEFAULTS, TEST_USER);
    const api = await request.newContext({
      baseURL: BASE_URL,
      extraHTTPHeaders: { Authorization: `Bearer ${ownerToken}` },
    });
    const res = await api.post(TICKET_PATH, { data: { projectId: projectIdOf(project) } });
    expect(res.status()).toBe(200);
    const body = await res.json();
    expect(typeof body.ticket).toBe('string');
    expect(body.ticket.length).toBeGreaterThan(15);
    expect(body.expiresIn).toBeGreaterThan(0);
    await api.dispose();
  });

  test('SEC3: the stream refuses connections without a live ticket', async ({ project }) => {
    const api = await request.newContext({ baseURL: BASE_URL });
    const streamPath = `/data/projects/${projectIdOf(project)}/events`;

    const noTicket = await api.get(streamPath);
    expect(noTicket.status()).toBe(401);

    const garbageTicket = await api.get(`${streamPath}?ticket=not-a-real-ticket`);
    expect(garbageTicket.status()).toBe(401);

    await api.dispose();
  });

  test('SEC4: an unauthenticated caller cannot mint tickets at all', async ({ project }) => {
    const api = await request.newContext({ baseURL: BASE_URL });
    const res = await api.post(TICKET_PATH, { data: { projectId: projectIdOf(project) } });
    expect(res.status()).toBe(401);
    await api.dispose();
  });
});
