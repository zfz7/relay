import React from 'react';
import {render, screen} from '@testing-library/react';
import {Header} from "./Header";

describe('Header', () => {
  it('renders app name in header', async () => {
    render(<Header />);
    const appName = screen.getByText(/Relay/);
    expect(appName).toBeInTheDocument();
  })
})