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
        expiration: new Date("10 October 2011 14:48"),
        address: "there"
      }, {
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
    expect(activeUsers.textContent).toEqual('Active users2');
  })

  it('renders table of users and sorts them', async () => {
    const {container} = render(<AdminPage/>);
    await act(async () => {
      await getPeersMock
    })
    const table = screen.getByTestId("peerTable")
    // eslint-disable-next-line testing-library/no-node-access
    const row1 = container!.querySelectorAll("tr")!![1]
    // eslint-disable-next-line testing-library/no-node-access
    const row2 = container!.querySelectorAll("tr")![2]
    expect(table.textContent).toContain('AddressExpiration');
    expect(row1.textContent).toContain('hereWed Oct 05 2011');
    expect(row2.textContent).toContain('thereMon Oct 10 2011');
  })
})