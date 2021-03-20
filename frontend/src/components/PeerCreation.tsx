import React, {useState} from 'react'
import {createPeer} from "../exchange/CreatePeer";
import {Peer} from "../exchange/types";
import {Button, Grid, Paper, Typography} from "@material-ui/core";
import {PeerDialog} from "./PeerDialog";
import {downloadPeer} from "../exchange/DownloadPeer";

export const PeerCreation: React.FC = () => {
  const [peer, setPeer] = useState<Peer>({id:"",expiration:new Date()})
  const [disabled, setDisabled] = useState<boolean>(false)
  const [open, setOpen] = useState<boolean>(false)

  const createNewPeer = () =>{
    setDisabled(true)
    createPeer()
      .then(response => setPeer(response))
      .then(()=> {
        setDisabled(false)
        setOpen(true)
      })
  }

  const download = () =>{
    downloadPeer(peer)
      .then(()=> {
        setOpen(false)
      })
  }
  return (<>
      <Paper elevation={2}>
        <Grid container spacing={2} direction="row" style={{margin:"1rem"}}>
          <Grid xs={12} item><Typography variant="h5" >getting started...</Typography></Grid>
          <Grid item><Button disabled={disabled} variant="contained" color="secondary" onClick={createNewPeer}>Connect</Button></Grid>
        </Grid>
      </Paper>
      <PeerDialog
        open={open}
        peer={peer}
        onClose={()=>setOpen(false)}
        onConfirm={download}
        />
    </>
  )
}

