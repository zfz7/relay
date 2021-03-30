import {saveAs} from "file-saver"
import {PeerConfigRequest} from "./types"

export const downloadPeer = async (peer: PeerConfigRequest) => {
  const promise = await fetch(`/api/peer/config`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(peer)
    }
  )

  const blob = await promise.blob()
  if(!promise.ok)
    return
  const filename = promise.headers.get('Content-Disposition')!.match(/filename="(.+\.conf)"/)![1]
  saveAs(blob, filename)
}
