import React, {useState} from 'react'
import {createPeer} from "../exchange/CreatePeer";
import {Peer} from "../exchange/types";
import {Button, Grid, LinearProgress, TextField, Typography} from "@mui/material";
import {PeerDialog} from "./PeerDialog";

export const PeerCreation: React.FC = () => {
  const [peer, setPeer] = useState<Peer>({id: "", expiration: new Date()})
  const [disabled, setDisabled] = useState<boolean>(false)
  const [open, setOpen] = useState<boolean>(false)
  const [code, setCode] = useState<string>("")
  const [error, setError] = useState<string>("")

  const createNewPeer = () => {
    setDisabled(true)
    createPeer({code: code})
      .then(response => setPeer(response))
      .then(() => {
          setError("")
          setDisabled(false)
          setOpen(true)

        },
        () => {
          setDisabled(false)
          setError("Incorrect Code")
        })
  }


  return (<>
      <Grid container spacing={2} direction="column" alignItems="center" justifyContent="center" style={{width: "100%", marginLeft:'0'}}>
        <Grid item sx={{mt:"1rem"}}>
          <Typography variant={"body2"}>please enter your pre-shared code</Typography>
        </Grid>
        <Grid item>
          <TextField
            required
            error={!!error}
            helperText={error}
            placeholder="code"
            id="standard-basic"
            label="code"
            type="password"
            value={code}
            onChange={(event) => setCode(event.target.value)}
          />
        </Grid>
      {disabled &&<LinearProgress style={{ width: '100%', marginTop:'1rem' }}value={100} color="primary"/>}
        <Grid item>
          <Button disabled={disabled || !code} variant="contained" color="primary"
                  onClick={createNewPeer}>create</Button>
        </Grid>
      </Grid>
      <PeerDialog
        open={open}
        peer={peer}
        onClose={() => setOpen(false)}
      />
    </>
  )
}

