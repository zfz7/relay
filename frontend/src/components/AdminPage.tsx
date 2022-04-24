import React, {useEffect, useState} from 'react'
import {Header} from "./Header";
import {getPeers} from "../exchange/GetPeers";
import {Logs, Peers} from "../exchange/types";
import {
  Card, CardContent, Container, Grid, Paper, Table,
  TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, useTheme
} from "@mui/material";
import {getLogs} from "../exchange/GetLogs";

export const AdminPage: React.FC = () => {
  const [peers, setPeers] = useState<Peers>()
  const [logs, setLogs] = useState<Logs>()

  const theme = useTheme()

  useEffect(() => {
    getPeers().then((dto) => {
      setPeers(dto)
    })
    getLogs().then((dto) => {
      setLogs(dto)
    })
  }, [])
  return (<>
      <Header/>
      <Container>
        <Grid sx={{mt: '1rem'}} container direction="row" spacing={2}>
          <Grid item md={3}>
            <Card sx={{backgroundColor: theme.palette.primary.light}} raised data-testid="activeUsers">
              <CardContent>
                <Typography>Active users</Typography>
                <Typography variant="h3">{peers?.peers.length}</Typography>
              </CardContent>
            </Card>
            <Card sx={{backgroundColor: theme.palette.primary.light, mt: '1rem'}} raised data-testid="invalidAdmin">
              <CardContent>
                <Typography>Invalid Admin Attempts</Typography>
                <Typography variant="h3">{logs?.invalidAdminAccessEvents.length}</Typography>
              </CardContent>
            </Card>
            <Card sx={{backgroundColor: theme.palette.primary.light, mt: '1rem'}} raised data-testid="invalidCode">
              <CardContent>
                <Typography>Invalid Access Code Attempts</Typography>
                <Typography variant="h3">{logs?.invalidAccessCodeEvents.length}</Typography>
              </CardContent>
            </Card>
            <Card sx={{backgroundColor: theme.palette.primary.light, mt: '1rem'}} raised data-testid="removedPeers">
              <CardContent>
                <Typography>Removed Peers</Typography>
                <Typography variant="h3">{logs?.peerRemovedEvents.length}</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item md={9}>
            <TableContainer component={Paper}>
              <Table data-testid="peerTable">
                <TableHead>
                  <TableRow>
                    <TableCell align="left">Address</TableCell>
                    <TableCell align="right">Expiration</TableCell>
                  </TableRow>
                </TableHead>
                {peers && <TableBody>
                  {peers.peers.sort((a, b) => a.expiration.getTime() - b.expiration.getTime()).map((peer) => (
                    <TableRow
                      key={peer.address}
                    >
                      <TableCell align="left">{peer.address}</TableCell>
                      <TableCell align="right">{peer.expiration.toDateString()}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>}
              </Table>
            </TableContainer>
          </Grid>
        </Grid>
      </Container>
    </>
  )
}

