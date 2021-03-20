import {Peer} from "./types";

export const createPeer: () => Promise<Peer> = () =>
  fetch('/api/peer',{
    method: "POST",
  }).then(response => response.json())
    .then((dto: RawPeer) => { return {id: dto.id, expiration: new Date(dto.expiration)}})

interface RawPeer  {
  id: string
  expiration: number
}
