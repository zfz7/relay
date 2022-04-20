import {Peers} from "./types";

export const getPeers: () => Promise<Peers> = () =>
  fetch('/api/admin/peers', {
    method: "GET",
    headers: {'Content-Type': 'application/json'}
  }).then(response => response.json())
    .then((dto: RawPeers) => ({
      peers: dto.peers.map(it =>
        ({
          expiration: new Date(it.expiration),
          address: it.address
        }))
    }))


interface RawPeers {
  peers: RawPeer[]
}

interface RawPeer {
  expiration: number
  address: string
}