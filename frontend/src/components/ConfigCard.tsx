import React, {useEffect, useState} from 'react'
import {
  Button,
  Card,
  CardContent,
  Divider,
  FormControlLabel,
  Grid,
  Switch,
  TextField,
  Typography,
  useTheme
} from "@mui/material";
import {Config} from "../exchange/types";
import {getConfig} from "../exchange/GetConfig";
import {updateConfig} from "../exchange/UpdateConfig";

export const ConfigCard: React.FC = () => {
  const [config, setConfig] = useState<Config>()
  const [mode, setMode] = useState<'EDIT' | 'VIEW'>('VIEW')
  const theme = useTheme()

  useEffect(() => {
    getConfig().then((dto) => setConfig(dto))
  }, [])

  return (
    <Card sx={{backgroundColor: theme.palette.primary.light, mt: '1rem'}} raised data-testid="configCard">
      <CardContent>
        <Grid container>
          <Grid item xs={12}><Typography>Relay Config</Typography></Grid>
          <Grid item xs={12} sx={{mb: "1rem"}}><Divider/></Grid>
          {config && <TextField
              fullWidth
              type="number"
              inputProps={{min: "0", step: "1"}}
              placeholder="Client Valid Duration"
              label="Client Valid Duration"
              value={config.clientValidDuration.toString()}
              onChange={(event) =>
                setConfig(prev => ({...prev!, clientValidDuration: +event.target.value}))}
              disabled={mode !== 'EDIT'}/>}
          {config && <FormControlLabel
              sx={{m: "1rem 0 0 0"}}
              labelPlacement="start"
              control={<Switch
                onChange={(event) =>
                  setConfig(prev => ({...prev!, disableLogs: event.target.checked}))}
                checked={config.disableLogs}
                disabled={mode !== 'EDIT'}/>}
              label="Disable Logs"/>}
          <Grid item xs={12} container justifyContent="center" alignItems="center" sx={{mt: "1rem"}}>
            {mode === 'VIEW' &&
                <Button variant="outlined"
                        color="error"
                        fullWidth
                        onClick={() => setMode('EDIT')}>Edit Config</Button>}
            {mode === 'EDIT' &&
                <Button variant="outlined"
                        color="error"
                        fullWidth
                        onClick={() => {
                          setMode('VIEW')
                          updateConfig(config!).then()
                        }}>Save Config</Button>}
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  )
}

