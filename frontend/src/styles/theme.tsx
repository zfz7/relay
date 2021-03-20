import {createMuiTheme} from "@material-ui/core";

export const theme = createMuiTheme({
  spacing: 8,
  palette: {
    type: 'light',
    primary: {
      main: '#8fa3ad',
      light: '#bfd4df',
      dark: '#61747e',
      contrastText: '#000000'
    },
    secondary: {
      main: '#ff5722',
      light: '#ff8a50',
      dark: '#c41c00',
      contrastText: '#000000'
    },
  },
})
