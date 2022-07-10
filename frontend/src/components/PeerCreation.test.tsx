import React from 'react';
import {cleanup, render, screen} from '@testing-library/react';
import {HomePage} from "./HomePage";
import {createPeer} from "../exchange/CreatePeer";
import {act} from "react-dom/test-utils";
import userEvent from "@testing-library/user-event";
import {downloadPeer} from "../exchange/DownloadPeer";

jest.mock('../exchange/CreatePeer')
const createPeerMock = createPeer as jest.MockedFunction<typeof createPeer>

jest.mock('../exchange/DownloadPeer')
const downloadPeerMock = downloadPeer as jest.MockedFunction<typeof downloadPeer>


describe('PeerCreation', () => {
  beforeEach(async () => {
      createPeerMock.mockResolvedValue(
        {id: '072597dc-65c9-4a27-857f-60d2b70442de',
                expiration: new Date("2021-04-19T01:28:12.687689Z")})
    downloadPeerMock.mockResolvedValue("")
  })

  afterEach(() => {
    cleanup()
    createPeerMock.mockReset()
    downloadPeerMock.mockReset()
  })

  it('creates new peer once user clicks connect button', async () => {
    render(<HomePage/>)
    await userEvent.type(screen.getByPlaceholderText('code'), 'code')
    await userEvent.click(screen.getByText("create"))
    await act(async () => {
      await createPeerMock
    })
    expect(createPeerMock).toHaveBeenCalledWith({code:"code"})

    await userEvent.click(screen.getByText("download"))
    await act(async () => {
      await downloadPeerMock
    })
    expect(downloadPeerMock).toHaveBeenCalledTimes(1)
  })

  it('shows error when code is bad', async () => {
    createPeerMock.mockRejectedValue("")
    render(<HomePage/>)
    await userEvent.type(screen.getByPlaceholderText('code'), 'bad code')
    await userEvent.click(screen.getByText("create"))
    await act(async () => {
      await createPeerMock
    })
    expect(createPeerMock).toHaveBeenCalledWith({code: "bad code"})
    expect(screen.getByText('Incorrect Code')).toBeInTheDocument()
  })
})