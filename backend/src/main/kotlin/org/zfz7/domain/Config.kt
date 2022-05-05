package org.zfz7.domain

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

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