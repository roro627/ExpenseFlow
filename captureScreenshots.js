#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const puppeteer = require('puppeteer');

const targets = [
  'http://localhost:4200/role',
  'http://localhost:4200/employee',
  'http://localhost:4200/manager',
  'http://localhost:4200/finance',
];

const outputDir = path.join(__dirname, 'render');

async function ensureDirectory(dirPath) {
  await fs.promises.mkdir(dirPath, { recursive: true });
}

function deriveFilename(urlStr) {
  const { pathname, hostname } = new URL(urlStr);
  const segments = pathname.split('/').filter(Boolean);
  const basename = segments.length ? segments[segments.length - 1] : hostname || 'screenshot';
  return `${basename}.png`;
}

function extractRole(urlStr) {
  const { pathname } = new URL(urlStr);
  const segment = pathname.split('/').filter(Boolean).pop();
  if (!segment) return null;
  const roles = new Set(['employee', 'manager', 'finance']);
  return roles.has(segment) ? segment : null;
}

async function waitForPath(page, expectedPath) {
  await page.waitForFunction(
    (path) => window.location.pathname === path,
    {},
    expectedPath
  );
}

async function captureScreenshot(browser, url) {
  const page = await browser.newPage();
  await page.setViewport({ width: 1920, height: 1080, deviceScaleFactor: 1 });
  const role = extractRole(url);

  await page.evaluateOnNewDocument(() => localStorage.clear());
  if (role) {
    await page.evaluateOnNewDocument((value) => localStorage.setItem('expenseflow-role', value), role);
  }

  await page.goto(url, { waitUntil: 'networkidle2', timeout: 60000 });
  await waitForPath(page, new URL(url).pathname);

  const filepath = path.join(outputDir, deriveFilename(url));
  await page.screenshot({ path: filepath, fullPage: true });
  await page.close();
  console.log(`Saved ${filepath}`);
}

(async () => {
  await ensureDirectory(outputDir);
  const browser = await puppeteer.launch({ headless: 'new' });

  try {
    for (const url of targets) {
      await captureScreenshot(browser, url);
    }
  } catch (error) {
    console.error('Screenshot capture failed:', error);
    process.exitCode = 1;
  } finally {
    await browser.close();
  }
})();
