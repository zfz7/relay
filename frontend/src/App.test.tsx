import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders app', () => {
  render(<App />);
  const subTitle = screen.getByText(/A secure connection/i);
  expect(subTitle).toBeInTheDocument();
});
