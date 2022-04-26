import '@testing-library/cypress/add-commands'

Cypress.Commands.add('resetDatabase', () => cy.exec(`psql postgres://relay_test:relay_test@localhost:5433/relay_test < ../backend/src/test/resources/setup.sql`))
Cypress.Commands.add('resetCode', () => cy.exec(`psql postgres://relay_test:relay_test@localhost:5433/relay_test -c "INSERT INTO code (code, valid_until, created_date) VALUES ('journey-code', null, NOW());"`))