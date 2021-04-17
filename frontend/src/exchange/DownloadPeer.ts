import {saveAs} from "file-saver"
import {PeerConfigRequest} from "./types"

export const downloadPeer: (peer: PeerConfigRequest) => Promise<string> = async (peer) => {
  const promise = await fetch(`/api/peer/config`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(peer)
    }
  )

  if (!promise.ok)
    return ""
  const blob = await promise.blob()
  const filename = promise.headers.get('Content-Disposition')!.match(/filename="(.+\.conf)"/)![1]
  if (navigator.userAgent.match('CriOS')) {
    alert("For the best experience please use Safari on iOS to download the file")
    const file = new File([blob], filename, {
      type: 'text/plain;charset=utf-8'
    })
    saveAs(file, filename)
  } else {
    saveAs(blob, filename)
  }
  return blob.text()
}
