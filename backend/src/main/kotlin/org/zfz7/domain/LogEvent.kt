package org.zfz7.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class LogEvent(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val createdDate: Instant = Instant.now(),

  val message: String,
  val key1: String? = null,

  @Enumerated(EnumType.STRING)
  val logType: LogType
)

data class InvalidAdminAccessEvent(
  val createdDate: Instant = Instant.now(),
  val username: String,
)

fun InvalidAdminAccessEvent.toLogEvent(): LogEvent = LogEvent(
  createdDate = createdDate,
  message = "An invalid github user: $username, has tried to access the admin page at $createdDate",
  key1 = username,
  logType = LogType.INVALID_ADMIN_ACCESS
)

enum class LogType {
  INVALID_ADMIN_ACCESS
}