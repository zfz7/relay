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

    cy.findByText("2) create a client config").click()
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
    cy.findByText("2) create a client config").click()
    cy.findByPlaceholderText('code').type('bad-code')
    cy.findByText("create").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.request.body).to.deep.contain({code:"bad-code"})
      expect(xhr.response!.statusCode).to.eql(400)
    })
    cy.findByText("Incorrect Code").should("exist")
  })
})
