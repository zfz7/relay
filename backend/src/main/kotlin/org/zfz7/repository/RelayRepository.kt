package org.zfz7.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zfz7.domain.Peer
import org.zfz7.domain.Relay
import java.util.*

@Repository
interface RelayRepository : JpaRepository<Relay, Long> {
  fun findTopBy(): Relay?
}