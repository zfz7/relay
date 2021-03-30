describe('app', () => {

  before(() => {
    cy.intercept({path: '/api/peer', method: 'POST'}).as('createPeer')
    cy.intercept({path: '/api/peer/*', method: 'Get'}).as('downloadPeer')
  })

  beforeEach(() => {
    cy.resetDatabase()
  })

  it('lets users download a new connection file', () => {
    cy.visit('/')
    expect(cy.contains('Relay')).exist
    expect(cy.contains('a secure connection')).exist

    cy.findByText("2) create a client config").click()
    cy.findByText("let's get started").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.response!.statusCode).to.eql(201)
    })
    cy.findByText("download").click()
    cy.wait('@downloadPeer').should(xhr => {
      expect(xhr.response!.statusCode).to.eql(200)
    })
  })
})
