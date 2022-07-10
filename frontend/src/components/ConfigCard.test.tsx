import React from 'react';
import {render, screen} from '@testing-library/react';
import {act} from "react-dom/test-utils";
import userEvent from "@testing-library/user-event";
import {getConfig} from "../exchange/GetConfig";
import {updateConfig} from "../exchange/UpdateConfig";
import {ConfigCard} from "./ConfigCard";

jest.mock('../exchange/GetConfig')
const getConfigMock = getConfig as jest.MockedFunction<typeof getConfig>

jest.mock('../exchange/UpdateConfig')
const updateConfigMock = updateConfig as jest.MockedFunction<typeof updateConfig>

describe('ConfigCard', () => {

  beforeEach(() => {
    getConfigMock.mockResolvedValue({
      disableLogs: true,
      clientValidDuration: 180
    })
    updateConfigMock.mockResolvedValue()
  })
  it('displays config ', async () => {
    render(<ConfigCard/>);
    await act(async () => {
      await getConfigMock
    })
    expect(getConfigMock).toBeCalledTimes(1);
    const expiration = screen.getByPlaceholderText('Client Valid Duration');
    expect((expiration as HTMLInputElement).value).toEqual('180');
    const disableLogs = screen.getByRole('checkbox', {name: 'Disable Logs'});
    expect(disableLogs).toBeChecked()
  })

  it('can update config', async () => {
    render(<ConfigCard/>);
    await act(async () => {
      await getConfigMock
    })
    await act(async () => {
      await screen.getByText("Edit Config").click()
    })
    await userEvent.type(screen.getByPlaceholderText('Client Valid Duration'), '9')
    await userEvent.click(screen.getByRole('checkbox', {name: 'Disable Logs'}))

    const expiration = screen.getByPlaceholderText('Client Valid Duration');
    expect((expiration as HTMLInputElement).value).toEqual('1809');
    const disableLogs = screen.getByRole('checkbox', {name: 'Disable Logs'});
    expect(disableLogs).not.toBeChecked()
    await act(async () => {
      screen.getByText("Save Config").click()
    })
    await act(async () => {
      await updateConfigMock
    })
    expect(updateConfigMock).toBeCalledWith(  {disableLogs: false, clientValidDuration: 1809})
  })
})