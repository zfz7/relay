package org.zfz7.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
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

  val expiration: Instant = Instant.now().plus(180, ChronoUnit.DAYS),
  //[Interface]
  val address: String,
  val privateKey: String,
  //[Peer]
  val allowedIps: String = "0.0.0.0/0,::/0",
  val endPoint: String ="relay.zfz7.org:51820",
  val preSharedKey: String,
  val publicKey: String,
)