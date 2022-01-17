import React from 'react'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom"
import {HomePage} from "./components/HomePage"
import {ThemeProvider} from '@material-ui/styles'
import {theme} from "./styles/theme";
import {Container} from "@material-ui/core"

export const App: React.FC = () => <ThemeProvider theme={theme}>
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
      </Routes>
    </Container>
  </Router>
</ThemeProvider>

export default App