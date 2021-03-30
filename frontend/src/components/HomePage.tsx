import React from 'react'
import {Header} from "./Header";
import {PeerCreation} from "./PeerCreation";
import {Accordion, AccordionDetails, AccordionSummary, Box, Grid, Link, Paper, Typography} from "@material-ui/core";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

export const HomePage: React.FC = () => {
  return (<>
      <Header/>
      <Paper elevation={2}>
        <Grid container spacing={2} direction="row" style={{padding: "1rem"}}>
          <Grid xs={12} item><Typography variant="h5">who we are...</Typography></Grid>
          <Grid xs={12} item>
            <Typography variant="body1">
              we provide a vpn service, based on the most advanced <a
              href="https://www.wireguard.com">WireGuard®</a> vpn protocol. By enabling our service you will protect all
              your internet traffic, and be able to access wider web using an US ip address.
            </Typography>
          </Grid>
          <Grid xs={12} item><Typography variant="h5">getting started...</Typography></Grid>
          <Grid xs={12} item>
            <Accordion square>
              <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography>1) install the official WireGuard VPN client</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <Grid>
                  <Typography>You can download the official WireGuard clients from their website <Link
                    href="https://www.wireguard.com/install/">here</Link>, or click the links below:</Typography>
                  <Typography style={{margin: "1rem"}}>
                    <Link href="https://download.wireguard.com/windows-client/wireguard-installer.exe">Windows</Link>
                  </Typography>
                  <Typography style={{margin: "1rem"}}>
                    <Link href="https://itunes.apple.com/us/app/wireguard/id1451685025?ls=1&mt=12">Mac</Link>
                  </Typography>
                  <Typography style={{margin: "1rem"}}>
                    <Link href="https://itunes.apple.com/us/app/wireguard/id1441195209?ls=1&mt=8">iPhone</Link>
                  </Typography>
                  <Typography style={{margin: "1rem"}}>
                    <Link href="https://play.google.com/store/apps/details?id=com.wireguard.android">Android</Link>
                  </Typography>
                  <Typography style={{margin: "1rem"}}>
                    <Link href="https://www.wireguard.com/install/">Others</Link>
                  </Typography>
                </Grid>
              </AccordionDetails>
            </Accordion>
            <Accordion square>
              <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography>2) create a client config</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <PeerCreation/>
              </AccordionDetails>
            </Accordion>
            <Accordion square>
              <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography>3) add your client config to your vpn client</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <Grid>
                  <Box m={1}><Typography>The final step is to add you downloaded config to your WireGuard client. Do not share your
                    config file with anyone, create multiple configs for multiple devices</Typography></Box>
                  <Box m={1}><Typography>Import/add a tunnel from file and send the recently downloaded file to the client </Typography></Box>
                  <Box m={1}><Typography>Activate/connect to the tunnel </Typography></Box>
                </Grid>
              </AccordionDetails>
            </Accordion>
            <Accordion square>
              <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography>4) verify your Ip address</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <Typography>check out <Link href="https://duckduckgo.com/?q=whats+my+ip&t=hd&va=o&ia=answer">this
                  link</Link> to see where you Ip address located</Typography>
              </AccordionDetails>
            </Accordion>
          </Grid>
        </Grid>
      </Paper>
    </>
  )
}

