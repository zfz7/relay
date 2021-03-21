package org.zfz7.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.zfz7.exchange.PeerDTO
import java.security.PrivateKey
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Relay(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val address: String = "10.8.0.1/32",
  val listenPort: String = "51820",
  val privateKey: String,
  val publicKey: String,
)