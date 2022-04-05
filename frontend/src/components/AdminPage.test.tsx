import React from 'react';
import {render, screen} from '@testing-library/react';
import {AdminPage} from "./AdminPage";
import {getAdmin} from "../exchange/GetAdmin";
import {act} from "react-dom/test-utils";

jest.mock('../exchange/GetAdmin')
const getAdminMock = getAdmin as jest.MockedFunction<typeof getAdmin>

describe('AdminPage', () => {

  beforeEach(()=>{
    getAdminMock.mockResolvedValue({count: 1})
  })

  it('renders number of users', async () => {
    render(<AdminPage />);
    await act(async () => {
      await getAdminMock
    })
    const appName = screen.getByText(/You have 1 users/);
    expect(appName).toBeInTheDocument();
  })
})