import React, {useState} from 'react'
import {createPeer} from "../exchange/CreatePeer";
import {Peer} from "../exchange/types";
import {Button, Grid, LinearProgress, TextField, Typography} from "@material-ui/core";
import {PeerDialog} from "./PeerDialog";
import {downloadPeer} from "../exchange/DownloadPeer";

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

  const download = () => {
    downloadPeer({id: peer.id})
      .then(() => {
        setOpen(false)
      })
  }
  return (<>
      <Grid container spacing={2} direction="column" alignItems="center" justify="center" style={{margin: "1rem"}}>
        <Grid item>
          <Typography variant={"body2"}>please enter your pre shared code</Typography>
        </Grid>
        <Grid item>
          <TextField
            required
            error={!!error}
            helperText={error}
            placeholder="code"
            id="standard-basic"
            label="code"
            value={code}
            onChange={(event) => setCode(event.target.value)}
          />
        </Grid>
        <Grid item>
          <Button disabled={disabled || !code} variant="contained" color="secondary"
                  onClick={createNewPeer}>create</Button>
        </Grid>
      {disabled &&<LinearProgress style={{ width: '100%' }}value={100} color="secondary"/>}
      </Grid>
      <PeerDialog
        open={open}
        peer={peer}
        onClose={() => setOpen(false)}
        onConfirm={download}
      />
    </>
  )
}

