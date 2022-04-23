import {Logs} from "./types";

export const getLogs: () => Promise<Logs> = () =>
  fetch('/api/admin/logs', {
    method: "GET",
    headers: {'Content-Type': 'application/json'}
  }).then(response => response.json())
    .then((dto: RawLogs) => ({
      invalidAdminAccessEvents: dto.invalidAdminAccessEvents.map(it =>
        ({
          ...it,
          createdDate: new Date(it.createdDate),
        })),
      invalidAccessCodeEvents: dto.invalidAccessCodeEvents.map(it =>
        ({
          ...it,
          createdDate: new Date(it.createdDate),
        })),
      peerRemovedEvents: dto.peerRemovedEvents.map(it =>
        ({
          ...it,
          createdDate: new Date(it.createdDate),
        })),
    }))


interface RawLogs {
  invalidAdminAccessEvents: RawInvalidAdminAccessEvent[]
  invalidAccessCodeEvents: RawInvalidAccessCodeEvent[]
  peerRemovedEvents: RawPeerRemovedEvent[]
}

interface RawInvalidAdminAccessEvent {
  createdDate: number
  username: string
}

interface RawInvalidAccessCodeEvent {
  createdDate: number
  ipAddress: string
}

interface RawPeerRemovedEvent {
  createdDate: number
  peerAddress: string
}