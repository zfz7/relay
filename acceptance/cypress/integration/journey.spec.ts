describe('app', () => {

  beforeEach(() => {
    cy.intercept({path: '/api/peer/config', method: 'POST'}).as('downloadPeer')
    cy.intercept({path: '/api/peer', method: 'POST'}).as('createPeer')
    cy.resetDatabase()
    cy.resetCode()
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
    cy.findByTestId("activeUsers").should('have.text','Active users0')
    cy.findByTestId("invalidCode").should('have.text','Invalid Access Code Attempts0')
    cy.findByTestId("removedPeers").should('have.text','Removed Peers0')
    cy.findByTestId("invalidAdmin").should('have.text','Invalid Admin Attempts0')
    cy.get('tr').should('have.length', '1')
    cy.get('tr').eq(0).should('have.text', 'AddressExpiration')
    cy.visit('/')
    cy.findByPlaceholderText('code').type('journey-code')
    cy.findByText("create").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.response!.statusCode).to.eql(202)
    })
    cy.visit('/admin')
    cy.findByTestId("activeUsers").should('have.text','Active users1')
    cy.findByTestId("invalidCode").should('have.text','Invalid Access Code Attempts0')
    cy.get('tr').should('have.length', '2')
    cy.get('tr').eq(1).should('contain.text', '10.0.0.2/32')

    cy.visit('/')
    cy.findByPlaceholderText('code').type('wrong-code')
    cy.findByText("create").click()
    cy.wait('@createPeer').should((xhr) => {
      expect(xhr.response!.statusCode).to.eql(400)
    })
    cy.visit('/admin')
    cy.findByTestId("invalidCode").should('have.text','Invalid Access Code Attempts1')
  })

  it('Allows admin to edit and copy code', () => {
    cy.visit('/admin')
    cy.findByText("Show Code").click()
    cy.findByTestId("codeCard").should('contain.text','journey-code')
    cy.findByRole("button",{name: 'copy'}).click()
    cy.window().its('navigator.clipboard').invoke('readText').should('equal', 'Current code is: journey-code')
    cy.findByRole("button",{name: 'edit'}).click()
    cy.findByPlaceholderText("code").type("new-code")
    cy.findByRole("button",{name: 'save'}).click()
    cy.findByText("Show Code").click()
    cy.findByTestId("codeCard").should('contain.text','new-code')
    cy.findByRole("button",{name: 'copy'}).click()
    cy.window().its('navigator.clipboard').invoke('readText').should('equal', 'Current code is: new-code')
  })

  it('Allows admin to edit config', () => {
    cy.visit('/admin')
    cy.findByTestId("configCard").within(() =>{
      cy.findByPlaceholderText("Client Valid Duration").should('have.value','10')
      cy.findByRole('checkbox', {name: 'Disable Logs'}).should('not.be.checked')
      cy.findByText("Edit Config").click()

      cy.findByPlaceholderText("Client Valid Duration").type('{selectAll}20')
      cy.findByRole('checkbox', {name: 'Disable Logs'}).check()

      cy.findByPlaceholderText("Client Valid Duration").should('have.value','20')
      cy.findByLabelText("Disable Logs").should('be.checked')
      cy.findByText("Save Config").click()
    })
    cy.visit('/admin')
    cy.findByTestId("configCard").within(() =>{
      cy.findByPlaceholderText("Client Valid Duration").should('have.value','20')
      cy.findByRole('checkbox', {name: 'Disable Logs'}).should('be.checked')
    })
  })
})
