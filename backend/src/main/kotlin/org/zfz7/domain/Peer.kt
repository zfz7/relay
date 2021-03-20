package org.zfz7.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.zfz7.exchange.PeerDTO
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Peer(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val publicId: UUID = UUID.randomUUID(),

  var expiration: Instant = Instant.now().plus(30, ChronoUnit.DAYS)
)