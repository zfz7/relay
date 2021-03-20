import React from 'react'
import {Header} from "./Header";
import {PeerCreation} from "./PeerCreation";

export const HomePage: React.FC = () => {
  return (<>
      <Header/>
      <PeerCreation/>
    </>
  )
}

