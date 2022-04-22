describe('app', () => {

  beforeEach(() => {
    cy.intercept({path: '/api/peer/config', method: 'POST'}).as('downloadPeer')
    cy.intercept({path: '/api/peer', method: 'POST'}).as('createPeer')
    cy.resetDatabase()
  })

  it('lets users download a new connection file', () => {
    cy.visit('/')
    expect(cy.contains('Relay')).exist
    expect(cy.contains('a secure connection')).exist

    cy.findByPlaceholderText('code').type('journey-code')
    cy.findByText("create").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.request.body).to.deep.contain({code:"journey-code"})
      expect(xhr.response!.statusCode).to.eql(202)
    })
    cy.findByText("download").click()
    cy.wait('@downloadPeer').should(xhr => {
      expect(xhr.response!.statusCode).to.eql(200)
    })
  })

  it('errors on bad code', () => {
    cy.visit('/')
    cy.findByPlaceholderText('code').type('bad-code')
    cy.findByText("create").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.request.body).to.deep.contain({code:"bad-code"})
      expect(xhr.response!.statusCode).to.eql(400)
    })
    cy.findByText("Incorrect Code").should("exist")
  })

  it('show correct user count on admin page', () => {
    cy.visit('/admin')
    cy.findByTestId("activeUsers") .should('have.text','Active users0')
    cy.get('tr').should('have.length', '1')
    cy.get('tr').eq(0).should('have.text', 'AddressExpiration')
    cy.visit('/')
    cy.findByPlaceholderText('code').type('journey-code')
    cy.findByText("create").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.response!.statusCode).to.eql(202)
    })
    cy.visit('/admin')
    cy.findByTestId("activeUsers") .should('have.text','Active users1')
    cy.get('tr').should('have.length', '2')
    cy.get('tr').eq(1).should('contain.text', '10.0.0.2/32')
  })
})
