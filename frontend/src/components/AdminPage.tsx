import React, {useEffect, useState} from 'react'
import {Header} from "./Header";
import {getPeers} from "../exchange/GetPeers";
import {Peers} from "../exchange/types";
import {Card, CardContent, Grid, Paper, Table,
  TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, useTheme} from "@mui/material";

export const AdminPage: React.FC = () => {
  const [peers, setPeers] = useState<Peers>()

  const theme = useTheme()

  useEffect(() => {
    getPeers().then((dto) => {
      setPeers(dto)
    })
  }, [])
  return (<>
      <Header/>
        <Grid sx={{mt:'1rem'}} container direction="row" spacing={2}>
          <Grid item xs={9}>
            <TableContainer component={Paper}>
              <Table sx={{ minWidth: 650 }} data-testid="peerTable" >
                <TableHead>
                  <TableRow>
                    <TableCell align="right">Address</TableCell>
                    <TableCell align="right">Expiration</TableCell>
                  </TableRow>
                </TableHead>
                {peers && <TableBody>
                  {peers.peers.map((peer) => (
                    <TableRow
                      key={peer.address}
                    >
                      <TableCell align="right">{peer.address}</TableCell>
                      <TableCell align="right">{peer.expiration.toDateString()}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>}
              </Table>
            </TableContainer>
          </Grid>
          <Grid item xs={3}>
            <Card sx={{backgroundColor: theme.palette.primary.light}} raised data-testid="activeUsers">
              <CardContent>
                <Typography>Active users</Typography>
                <Typography variant="h3">{peers?.peers.length}</Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
    </>
  )
}

