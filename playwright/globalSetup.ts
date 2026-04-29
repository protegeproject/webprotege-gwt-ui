import { FullConfig } from '@playwright/test';
import * as fs from 'fs';
import * as path from 'path';

/**
 * Stub globalSetup. Real implementation lands in commit 4 (`feat(e2e): add
 * globalSetup + storageState login`). For now we just ensure the .auth dir
 * exists and write an empty storage state so `playwright test --list` works
 * before the real auth flow is wired up.
 */
async function globalSetup(_config: FullConfig): Promise<void> {
  const authDir = path.join(__dirname, '.auth');
  if (!fs.existsSync(authDir)) fs.mkdirSync(authDir, { recursive: true });
  const stateFile = path.join(authDir, 'storageState.json');
  if (!fs.existsSync(stateFile)) {
    fs.writeFileSync(stateFile, JSON.stringify({ cookies: [], origins: [] }));
  }
}

export default globalSetup;
