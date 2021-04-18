import React, {useEffect, useState} from 'react'
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Typography,
} from '@material-ui/core'
import {Peer} from "../exchange/types";
import {GenerateQRCode} from "./GenerateQRCode";
import {downloadPeer} from "../exchange/DownloadPeer";

type PeerDialogProps = {
  open: boolean
  peer: Peer
  onClose: () => void
}

export const PeerDialog = ({peer, open, onClose}: PeerDialogProps) => {
  const [conf, setConf] = useState<string>("")
  const download = () => {
    downloadPeer({id: peer.id})
      .then((conf) => {
        setConf(conf)
      })
  }
  useEffect(()=>{
    if(open!){
      setConf("")
    }
  },[open])
  return (
    <Dialog
      data-testid="confirmDialog"
      open={open}
    >
      <DialogTitle disableTypography>
        <Typography variant="h6">relay</Typography>
      </DialogTitle>
      <DialogContent>
        <DialogContentText>{`Your connection will expire on ${peer.expiration.toDateString()}`}
        </DialogContentText>
        {!!conf && <GenerateQRCode text={conf}/>}
      </DialogContent>
      <DialogActions>
        <Button
            data-testid="cancelButton"
            onClick={onClose}>
            cancel
        </Button>
        <Button data-testid="confirmButton"
                variant="contained"
                color="secondary"
                onClick={download}>
          {!!conf? "download again" : "download"}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
