import React from "react"
import {AppBar, Grid, IconButton, Typography} from "@mui/material"
import logo from "../styles/images/logo-white.png"

export const Header: React.FC = () => {
  return (
    <AppBar
      color="primary"
      position="sticky"
      elevation={0}
    >
      <Grid container alignItems="center" style={{margin:"4px 0"}}>
        <Grid item>
          <IconButton size={"small"} edge="start" style={{marginLeft: "0.5rem"}}>
            <img src={logo} alt="Logo" width="40px"/>
          </IconButton>
        </Grid>
        <Grid item>
          <Typography variant="h4" display="inline">Relay</Typography>
          <Typography variant="subtitle1" display="inline">a secure connection</Typography>
        </Grid>
      </Grid>
    </AppBar>
  )
}
