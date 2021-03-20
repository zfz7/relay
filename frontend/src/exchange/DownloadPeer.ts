import {saveAs} from "file-saver"
import {Peer} from "./types"

export const downloadPeer = async (peer: Peer) => {
  const promise = await fetch(`/api/peer/${peer.id}`,
    {
      method: 'GET',
      headers: {
        "Accept": "application/text/plain",
      }
    }
  )

  const blob = await promise.blob()
  if(!promise.ok)
    return
  const filename = promise.headers.get('Content-Disposition')!.match(/filename="(.+\.conf)"/)![1]
  saveAs(blob, filename)
}
