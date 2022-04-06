package org.zfz7.exchange

import org.zfz7.domain.Peer
import java.time.Instant
import java.util.*

data class Peers(
  val peers: List<DetailedPeer>,
)

data class DetailedPeer(
  val id: UUID,
  val expiration: Instant,
  val address: String
)


fun Peer.toDetailedDto(): DetailedPeer = DetailedPeer(
  id = publicId,
  expiration = expiration,
  address = address
)
