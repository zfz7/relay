import {Peer, PeerRequest} from "./types";

export const createPeer: (request: PeerRequest) => Promise<Peer> = (request: PeerRequest) =>
  fetch('/api/peer',{
    method: "POST",
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(request)
  }).then(response => response.json())
    .then((dto: RawPeer) => { return {id: dto.id, expiration: new Date(dto.expiration)}})

interface RawPeer  {
  id: string
  expiration: number
}
