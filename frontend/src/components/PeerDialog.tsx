import React from 'react'
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

type PeerDialogProps = {
  open: boolean
  peer: Peer
  onConfirm: () => void
  onClose: () => void
}

export const PeerDialog = ({peer, open, onConfirm, onClose}: PeerDialogProps) => {
  return (
    <Dialog
      data-testid="confirmDialog"
      open={open}
    >
      <DialogTitle disableTypography>
        <Typography variant="h5">here is secure connection</Typography>
      </DialogTitle>
      <DialogContent>
        <DialogContentText>{`Your connection will expire ${peer.expiration.toDateString()}`}
        </DialogContentText>
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
                onClick={() => {
                  onConfirm()
                }}>
          download
        </Button>
      </DialogActions>
    </Dialog>
  )
}
