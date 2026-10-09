const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: process.env.CYPRESS_BASE_URL || 'http://localhost:8080',
    video: false,
    screenshotOnRunFailure: true,
    specPattern: 'cypress/e2e/**/*.cy.js'
  }
});
