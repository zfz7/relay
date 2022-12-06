package org.zfz7.domain

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Relay(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val address: String = "10.0.0.1",
  val listenPort: String,
  val privateKey: String,
  val publicKey: String,
)