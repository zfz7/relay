import React from 'react';
import {cleanup, render, screen} from '@testing-library/react';
import {HomePage} from "./HomePage";
import {createPeer} from "../exchange/CreatePeer";
import {act} from "react-dom/test-utils";
import userEvent from "@testing-library/user-event";
import {PeerDialog} from "./PeerDialog";


jest.mock('../exchange/CreatePeer')
const createPeerMock = createPeer as jest.MockedFunction<typeof createPeer>


describe('PeerCreation', () => {
  const peer = {id: '072597dc-65c9-4a27-857f-60d2b70442de',
    expiration: new Date("2021-04-19T01:28:12.687689")}
  it('it renders ', async () => {
    render(<PeerDialog peer={peer} open={true} onClose={jest.fn()} onConfirm={jest.fn()}/>);
    const title = screen.getByText(/secure connection/);
    expect(title).toBeInTheDocument();
    const expiration = screen.getByText(/Sun Apr 19 2021/);
    expect(expiration).toBeInTheDocument();

  })
  it('it calls on cancel', async () => {
    const onClose = jest.fn()
    render(<PeerDialog peer={peer} open={true} onClose={onClose} onConfirm={jest.fn()}/>);
    userEvent.click(screen.getByText("cancel"))
    expect(onClose).toHaveBeenCalledTimes(1)
  })
  it('it calls onConfirm', async () => {
    const onClose = jest.fn()
    const onConfirm = jest.fn()
    render(<PeerDialog peer={peer} open={true} onClose={onClose} onConfirm={onConfirm}/>);
    userEvent.click(screen.getByText("download"))
    expect(onConfirm).toHaveBeenCalledTimes(1)
  })
})