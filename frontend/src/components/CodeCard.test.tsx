import React from 'react';
import {render, screen} from '@testing-library/react';
import {CodeCard} from "./CodeCard";
import {getCode} from "../exchange/GetCode";
import {act} from "react-dom/test-utils";
import {updateCode} from "../exchange/UpdateCode";
import userEvent from "@testing-library/user-event";

jest.mock('../exchange/GetCode')
const getCodeMock = getCode as jest.MockedFunction<typeof getCode>

jest.mock('../exchange/UpdateCode')
const updateCodeMock = updateCode as jest.MockedFunction<typeof updateCode>

describe('CodeCard', () => {

  beforeEach(() => {
    getCodeMock.mockResolvedValue({
      code: "test-code"
    })
    updateCodeMock.mockResolvedValue()
  })
  it('makes call to get code and displays code ', async () => {
    render(<CodeCard/>);
    expect(getCodeMock).toBeCalledTimes(0);
    screen.getByText("Show Code").click()
    expect(getCodeMock).toBeCalledTimes(1);
    await act(async () => {
      await getCodeMock
    })
    const codeCard = screen.getByTestId('codeCard');
    expect(codeCard.textContent).toContain('test-code');
  })

  it('can update code', async () => {
    render(<CodeCard/>);
    screen.getByText("Show Code").click()
    await act(async () => {
      await getCodeMock
    })
    screen.getByRole('button', {name: 'edit'}).click()
    userEvent.type(screen.getByPlaceholderText('code'), 'new-code')
    screen.getByRole('button', {name: 'save'}).click()
    await act(async () => {
      await updateCodeMock
    })
    expect(updateCodeMock).toBeCalledWith('new-code')
  })
})