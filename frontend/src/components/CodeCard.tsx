import React, {useState} from 'react'
import {Button, Card, CardContent, Divider, Grid, IconButton, TextField, Typography, useTheme} from "@mui/material";
import {getCode} from "../exchange/GetCode";
import {updateCode} from "../exchange/UpdateCode";
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';

export const CodeCard: React.FC = () => {
  const [code, setCode] = useState<string>()
  const [newCode, setNewCode] = useState<string>("")
  const [mode, setMode] = useState<'DEFAULT' | 'EDIT' | 'VIEW'>('DEFAULT')
  const theme = useTheme()

  return (
    <Card sx={{backgroundColor: theme.palette.primary.light}} raised data-testid="codeCard">
      <CardContent>
        <Grid container>
          <Grid item xs={12}><Typography>View/Edit Code</Typography></Grid>
          <Grid item xs={12} sx={{mb: "1rem"}}><Divider/></Grid>
          {mode === 'VIEW' && <Grid item xs={12} container justifyContent="space-between" alignItems="center">
              <Typography variant="subtitle2" display="inline" fontFamily="monospace"
                          sx={{backgroundColor: "grey"}}>{code}</Typography>
              <div><IconButton aria-label={"copy"}
                               onClick={() => navigator.clipboard.writeText(`Current code is: ${code}`)}><ContentCopyIcon/></IconButton>
                  <IconButton aria-label={"edit"} onClick={() => setMode('EDIT')}><EditIcon/></IconButton></div>
          </Grid>}
          {mode === 'EDIT' && <Grid item xs={12} container justifyContent="space-between" alignItems="center">
              <TextField placeholder="code" value={newCode}
                         onChange={(event) => setNewCode(event.target.value)}/>
              <IconButton aria-label={"save"} disabled={!newCode}
                          onClick={() => updateCode(newCode!).then(() => setMode('DEFAULT'))}><SaveIcon/></IconButton>
          </Grid>}
          <Grid item xs={12} container justifyContent="center" alignItems="center" sx={{mt: "1rem"}}>
            {mode === 'DEFAULT' && <Button variant="outlined" color="error" fullWidth onClick={() => {
              getCode().then(dto => {
                setCode(dto.code)
                setMode('VIEW')
              })
            }}>Show Code</Button>}
            {mode !== 'DEFAULT' &&
                <Button variant="outlined" color="error" fullWidth onClick={() => setMode('DEFAULT')}>
                    Hide Code</Button>}
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  )
}

