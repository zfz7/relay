import React, {useState} from 'react'
import {createPeer} from "../exchange/CreatePeer";
import {Peer} from "../exchange/types";
import {Button, Grid} from "@material-ui/core";
import {PeerDialog} from "./PeerDialog";
import {downloadPeer} from "../exchange/DownloadPeer";

export const PeerCreation: React.FC = () => {
  const [peer, setPeer] = useState<Peer>({id: "", expiration: new Date()})
  const [disabled, setDisabled] = useState<boolean>(false)
  const [open, setOpen] = useState<boolean>(false)

  const createNewPeer = () => {
    setDisabled(true)
    createPeer()
      .then(response => setPeer(response))
      .then(() => {
        setDisabled(false)
        setOpen(true)
      })
  }

  const download = () => {
    downloadPeer(peer)
      .then(() => {
        setOpen(false)
      })
  }
  return (<>
      <Grid container spacing={2} direction="row" style={{margin: "1rem"}}>
        <Grid item><Button disabled={disabled} variant="contained" color="secondary"
                           onClick={createNewPeer}>let's get started</Button></Grid>
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

