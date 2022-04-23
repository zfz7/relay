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
  expiration: Date
  address: string
}

export interface Logs {
  invalidAdminAccessEvents: InvalidAdminAccessEvent[]
  invalidAccessCodeEvents: InvalidAccessCodeEvent[]
  peerRemovedEvents: PeerRemovedEvent[]
}

export interface InvalidAdminAccessEvent {
  createdDate: Date
  username: string
}

export interface InvalidAccessCodeEvent {
  createdDate: Date
  ipAddress: string
}

export interface PeerRemovedEvent {
  createdDate: Date
  peerAddress: string
}