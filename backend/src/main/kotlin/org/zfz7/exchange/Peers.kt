package org.zfz7.exchange

import org.zfz7.domain.Peer
import java.time.Instant

data class Peers(
  val peers: List<DetailedPeer>,
)

data class DetailedPeer(
  val expiration: Instant,
  val address: String
)


fun Peer.toDetailedDto(): DetailedPeer = DetailedPeer(
  expiration = expiration,
  address = address
)
