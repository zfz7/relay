import React from "react"
import {AppBar, Grid, IconButton, Typography, useTheme} from "@material-ui/core"
import logo from "../styles/images/logo.png"

export const Header: React.FC = () => {
  const theme = useTheme()
  return (
    <AppBar
      color="inherit"
      style={{
        backgroundColor: theme.palette.primary.light,
      }}
      position="sticky"
      elevation={1}
    >
      <Grid container alignItems="center" style={{margin:"4px"}}>
        <Grid item>
          <IconButton size={"small"} edge="start" style={{marginLeft: "1rem"}}>
            <img src={logo} alt="Logo" width="40px"/>
          </IconButton>
        </Grid>
        <Grid item>
          <Typography variant="h4" display="inline">Relay</Typography>
          <Typography variant="subtitle1" display="inline">A secure connection</Typography>
        </Grid>
      </Grid>
    </AppBar>
  )
}
