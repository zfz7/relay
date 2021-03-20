package org.zfz7.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zfz7.domain.Peer
import java.util.*

@Repository
interface PeerRepository : JpaRepository<Peer, Long> {
  fun findByPublicId(peerId: UUID): Peer?
}