import React from 'react';
import {render, screen} from '@testing-library/react';
import {downloadPeer} from "../exchange/DownloadPeer";
import userEvent from "@testing-library/user-event";
import {PeerDialog} from "./PeerDialog";


jest.mock('../exchange/DownloadPeer')
const downloadPeerMock = downloadPeer as jest.MockedFunction<typeof downloadPeer>


describe('PeerCreation', () => {
  const peer = {id: '072597dc-65c9-4a27-857f-60d2b70442de',
    expiration: new Date("2021-04-19T01:28:12.687689")}
  beforeEach(()=>{
    downloadPeerMock.mockResolvedValue("")
  })
  it('it renders ', async () => {
    render(<PeerDialog peer={peer} open={true} onClose={jest.fn()}/>);
    const title = screen.getByText("relay");
    expect(title).toBeInTheDocument();
    const expiration = screen.getByText(/Mon Apr 19 2021/);
    expect(expiration).toBeInTheDocument();
  })
  it('it calls on cancel', async () => {
    const onClose = jest.fn()
    render(<PeerDialog peer={peer} open={true} onClose={onClose}/>);
    await userEvent.click(screen.getByText("cancel"))
    expect(onClose).toHaveBeenCalledTimes(1)
  })
  it('it calls onConfirm', async () => {
    const onClose = jest.fn()
    render(<PeerDialog peer={peer} open={true} onClose={onClose}/>);
    await userEvent.click(screen.getByText("download"))
    expect(downloadPeerMock).toHaveBeenCalledTimes(1)
  })
})