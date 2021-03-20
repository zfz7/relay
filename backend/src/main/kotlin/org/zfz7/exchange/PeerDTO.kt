package org.zfz7.exchange

import org.zfz7.domain.Peer
import java.time.Instant
import java.util.*

data class PeerDTO(
  val id: UUID,
  val expiration: Instant
)


fun Peer.toDto(): PeerDTO = PeerDTO(
  id = publicId,
  expiration = expiration
)
