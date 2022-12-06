package org.zfz7.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class Config(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val disableLogs: Boolean,
  val clientValidDuration: Int,

  val createdDate: Instant = Instant.now(),
  val validUntil: Instant? = null,
)