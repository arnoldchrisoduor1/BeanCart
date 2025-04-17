const path = require('path');
const serverBuildPath = path.join(__dirname, '../../dist/frontend/server/main.js');

// Import the server module
const serverModule = require(serverBuildPath);

exports.handler = async (event, context) => {
  // If there's a direct handler export, use it
  if (typeof serverModule.handler === 'function') {
    return serverModule.handler(event, context);
  }
  
  // Otherwise, try to create a serverless handler from the Express app
  const serverless = require('serverless-http');
  const app = serverModule.default || serverModule.app || serverModule;
  
  // Create a serverless wrapper for the app
  const handler = serverless(app);
  return handler(event, context);
};