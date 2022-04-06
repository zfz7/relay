export interface Peer {
  id: string,
  expiration: Date
}

export interface PeerRequest {
  code: string,
}

export interface PeerConfigRequest {
  id: string,
}

export interface Peers {
  peers: DetailedPeer[],
}

export interface DetailedPeer {
  id: string,
  expiration: Date
  address: string
}