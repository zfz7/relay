import React from 'react';
import {render, screen} from '@testing-library/react';
import {AdminPage} from "./AdminPage";
import {getPeers} from "../exchange/GetPeers";
import {act} from "react-dom/test-utils";

jest.mock('../exchange/GetPeers')
const getPeersMock = getPeers as jest.MockedFunction<typeof getPeers>

describe('AdminPage', () => {

  beforeEach(() => {
    getPeersMock.mockResolvedValue({
      peers: [{
        id: 'thing',
        expiration: new Date("05 October 2011 14:48"),
        address: "here"
      }]
    })
  })

  it('renders number of users', async () => {
    render(<AdminPage/>);
    await act(async () => {
      await getPeersMock
    })
    const activeUsers = screen.getByTestId('activeUsers');
    expect(activeUsers.textContent).toEqual('Active users1');
  })

  it('renders table of users', async () => {
    render(<AdminPage/>);
    await act(async () => {
      await getPeersMock
    })
    const table = screen.getByTestId("peerTable")
    expect(table.textContent).toContain('UUIDAddressExpiration');
    expect(table.textContent).toContain('thinghereWed Oct 05 2011');
  })
})