import React from 'react'
import {Header} from "./Header";
import {PeerCreation} from "./PeerCreation";
import {Button, Card, CardContent, Grid, Link, List, ListItem, Typography} from "@mui/material";
import SecurityIcon from '@mui/icons-material/Security';
import LockIcon from '@mui/icons-material/Lock';
import CellWifiIcon from '@mui/icons-material/CellWifi';
import MobileFriendlyIcon from '@mui/icons-material/MobileFriendly';
import map from "../styles/images/global-digital-map.webp"
import windows from "../styles/images/windows.webp"
import mac from "../styles/images/mac.webp"
import ios from "../styles/images/ios.webp"
import android from "../styles/images/android.webp"

export const HomePage: React.FC = () => {

  return (<>
      <Header/>
      <Grid container direction="row">
        <Grid xs={12} container item sx={{height: 'calc(100vh - 58px)', backgroundColor: '#0E0C19'}}>
          <Grid sm={6} container item alignItems="center" justifyContent="center">
            <Typography variant="h4" color="white">access the whole internet</Typography>
          </Grid>
          <Grid sm={6} container item alignItems="center" justifyContent="left">
            <img src={map} alt="digital map" width="100%"/>
          </Grid>
        </Grid>
        <div style={{backgroundColor: "#757575", width: '100%', height: '1rem'}}/>
        <Grid xs={12} container item sx={{height: '75vh', backgroundColor: '#fafafa'}}>
          <Grid sm={6} container item alignItems="center" justifyContent="center">
            <List>
              <ListItem><SecurityIcon sx={{mr: '1rem'}}/><Typography variant="h5">security</Typography></ListItem>
              <ListItem><LockIcon sx={{mr: '1rem'}}/><Typography variant="h5">privacy</Typography></ListItem>
              <ListItem><CellWifiIcon sx={{mr: '1rem'}}/><Typography variant="h5">access</Typography></ListItem>
              <ListItem><MobileFriendlyIcon sx={{mr: '1rem'}}/><Typography variant="h5">mobile</Typography></ListItem>
            </List>
          </Grid>
          <Grid sm={6} container item alignItems="center" justifyContent="center">
            <Typography variant="subtitle1" sx={{width: '65%'}}>
              We provide a vpn service, based on the most advanced <a
              href="https://www.wireguard.com">WireGuard®</a> vpn protocol. By enabling our service you will protect all
              your internet traffic, and be able to access wider web using an US ip address.
            </Typography>
          </Grid>
        </Grid>
        <div style={{backgroundColor: "#757575", width: '100%', height: '1rem'}}/>
        <Grid xs={12} container spacing={2} item sx={{backgroundColor: '#0E0C19',pb:'2rem'}} alignItems="start">
          <Grid xs={12} container item justifyContent="center">
            <Typography variant="h4" color="white">lets get started!</Typography>
          </Grid>
          <Grid sm={6} container item justifyContent="center" justifyItems="start" alignItems="top">
            <Card square sx={{width: '35rem', height: '15rem'}}>
              <CardContent>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Step 1:
                </Typography>
                <Typography variant="h5" component="div">
                  Install VPN client
                </Typography>
                <Typography>Download the official <Link
                  href="https://www.wireguard.com/install/">WireGuard clients</Link>, or click the links below:</Typography>
                <List>
                  <ListItem>
                    <Button variant="contained" sx={{marginRight: '1rem'}}
                            href="https://itunes.apple.com/us/app/wireguard/id1451685025?ls=1&mt=12">
                      <img src={mac} alt="mac logo" width="25px" style={{marginRight: '.75rem'}}/>Mac
                    </Button>
                    <Button variant="contained"
                            href="https://download.wireguard.com/windows-client/wireguard-installer.exe">
                      <img src={windows} alt="windows logo" width="25px" style={{marginRight: '.75rem'}}/>Windows
                    </Button>
                  </ListItem>
                  <ListItem>
                    <Button variant="contained" sx={{marginRight: '1rem'}}
                            href="https://itunes.apple.com/us/app/wireguard/id1441195209?ls=1&mt=8">
                      <img src={ios} alt="ios logo" width="25px" style={{marginRight: '.75rem'}}/>iPhone
                    </Button>
                    <Button variant="contained" sx={{marginRight: '1rem'}}
                            href="https://play.google.com/store/apps/details?id=com.wireguard.android">
                      <img src={android} alt="andriod logo" width="25px" style={{marginRight: '.75rem'}}/>Android
                    </Button>
                    <Button variant="contained"
                            href="https://www.wireguard.com/install/">Other Platforms
                    </Button>
                  </ListItem>
                </List>
              </CardContent>
            </Card>
          </Grid>
          <Grid sm={6} container item justifyContent="center" justifyItems="start" alignItems="top">
            <Card square sx={{width: '35rem', height: '20rem'}}>
              <CardContent>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Step 2:
                </Typography>
                <Typography variant="h5" component="div">
                  Download VPN credentials
                </Typography>
                <Typography>To download your VPN credentials please enter the pre-shared code</Typography>
                <PeerCreation/>
              </CardContent>
            </Card>
          </Grid>
          <Grid sm={6} container item justifyContent="center" justifyItems="start" alignItems="top">
            <Card square sx={{width: '35rem', height: '20rem'}}>
              <CardContent>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Step 3:
                </Typography>
                <Typography variant="h5" component="div">
                  Add your credentials
                </Typography>
                <Typography>Please add your downloaded config to your WireGuard client. Do not
                  share your config file with anyone. If needed please create multiple configs for multiple
                  devices</Typography>
                <Typography sx={{mt: '1rem'}}>Import/add a tunnel from file and send the recently downloaded file to the
                  client </Typography>
                <Typography sx={{mt: '1rem'}}>Activate/connect to the tunnel</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid sm={6} container item justifyContent="center" justifyItems="start" alignItems="top">
            <Card square sx={{width: '35rem', height: '15rem'}}>
              <CardContent>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Step 4:
                </Typography>
                <Typography variant="h5" component="div">
                  Verify your new IP Address
                </Typography>
                <Typography>Click on <Link href="https://duckduckgo.com/?q=whats+my+ip&t=hd&va=o&ia=answer">this
                  link</Link> to see where your new IP address is located. It should say somewhere in the
                  USA.</Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Grid>
    </>
  )
}

