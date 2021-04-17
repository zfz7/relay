import {useEffect, useRef} from "react";

type QRCodeProps = {
  text: string
}

export const GenerateQRCode = ({text}: QRCodeProps) => {
  const QRCode = require('qrcode');
  const canvas = useRef(null);
  useEffect(() => {
    if (canvas != null && canvas.current != null) {
      QRCode.toCanvas(document.getElementById('canvas'),
        text, { toSJISFunc: QRCode.toSJIS }, (error: Error) => {if(error) console.error(error)})
    }
  });
  return(<canvas id="canvas" ref={canvas}/>);
}