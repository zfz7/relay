import React from 'react'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom"
import {HomePage} from "./components/HomePage"
import { ThemeProvider,  StyledEngineProvider } from '@mui/material';
import {theme} from "./styles/theme";
import {Container} from "@mui/material"
import {AdminPage} from "./components/AdminPage";


export const App: React.FC = () => <StyledEngineProvider injectFirst>
  <ThemeProvider theme={theme}>
    <Router>
      <Container style={{
        maxWidth: '100vw',
        minHeight: '100vh',
        maxHeight: '100vh',
        display: "flex",
        flexDirection: "column",
        backgroundColor: theme.palette.background.default
      }}>
        <Routes>
          <Route path="/" element={<HomePage/>}/>
          <Route path="/admin" element={<AdminPage/>}/>
        </Routes>
      </Container>
    </Router>
  </ThemeProvider>
</StyledEngineProvider>

export default App