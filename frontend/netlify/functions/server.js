const { handler } = require('../../dist/frontend/server/server');

exports.handler = async (event, context) => {
  return await handler(event, context);
};