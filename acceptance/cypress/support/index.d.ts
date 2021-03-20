/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject> {
    resetDatabase(): void
  }
}
