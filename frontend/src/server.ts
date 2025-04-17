import { APP_BASE_HREF } from '@angular/common';
import { CommonEngine } from '@angular/ssr/node';
import express from 'express';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import bootstrap from './main.server';
import serverless from 'serverless-http';
import compression from 'compression';

// Get the directory paths
const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');
const indexHtml = join(serverDistFolder, 'index.server.html');

// Create Express app
const app = express();
const commonEngine = new CommonEngine();

// Add compression middleware
app.use(compression());

// Serve static files with caching
app.get(
  '*.*',
  express.static(browserDistFolder, {
    maxAge: '1y'
  })
);

// Handle all routes for Angular app
app.get('*', (req, res, next) => {
  const { protocol, originalUrl, baseUrl, headers } = req;

  commonEngine
    .render({
      bootstrap,
      documentFilePath: indexHtml,
      url: `${protocol}://${headers.host}${originalUrl}`,
      publicPath: browserDistFolder,
      providers: [{ provide: APP_BASE_HREF, useValue: baseUrl }],
    })
    .then((html) => res.send(html))
    .catch((err) => {
      console.error('Error rendering app:', err);
      next(err);
    });
});

// For local development
if (process.env['NODE_ENV'] !== 'production') {
  const port = process.env['PORT'] || 4000;
  app.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

// For Netlify Functions
export const handler = serverless(app);

// Export app for testing or other purposes
export default app;