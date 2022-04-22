import React from 'react'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom"
import {HomePage} from "./components/HomePage"
import {StyledEngineProvider, ThemeProvider} from '@mui/material';
import {theme} from "./styles/theme";
import {AdminPage} from "./components/AdminPage";


export const App: React.FC = () => <StyledEngineProvider injectFirst>
  <ThemeProvider theme={theme}>
    <Router>
        <Routes>
          <Route path="/" element={<HomePage/>}/>
          <Route path="/admin" element={<AdminPage/>}/>
        </Routes>
    </Router>
  </ThemeProvider>
</StyledEngineProvider>

export default App