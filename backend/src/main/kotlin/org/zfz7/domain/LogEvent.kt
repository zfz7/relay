package org.zfz7.domain

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

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
  message = "[$createdDate] An invalid github user: $username, has tried to access the admin page.",
  key1 = username,
  logType = LogType.INVALID_ADMIN_ACCESS
)
fun LogEvent.toInvalidAdminAccessEvent(): InvalidAdminAccessEvent = InvalidAdminAccessEvent(
  createdDate = createdDate,
  username = key1 ?: "null"
)

data class InvalidAccessCodeEvent(
  val createdDate: Instant = Instant.now(),
  val ipAddress: String,
)

fun InvalidAccessCodeEvent.toLogEvent(): LogEvent = LogEvent(
  createdDate = createdDate,
  message = "[$createdDate] An invalid credential request from this address: $ipAddress",
  key1 = ipAddress,
  logType = LogType.INVALID_ACCESS_CODE
)

fun LogEvent.toInvalidAccessCodeEvent(): InvalidAccessCodeEvent = InvalidAccessCodeEvent(
  createdDate = createdDate,
  ipAddress = key1 ?: "null"
)
data class PeerRemovedEvent(
  val createdDate: Instant = Instant.now(),
  val peerAddress: String,
)

fun PeerRemovedEvent.toLogEvent(): LogEvent = LogEvent(
  createdDate = createdDate,
  message = "[$createdDate] An expired peer was removed. Peer address: $peerAddress",
  key1 = peerAddress,
  logType = LogType.PEER_REMOVED
)

fun LogEvent.toPeerRemovedEvent(): PeerRemovedEvent = PeerRemovedEvent(
  createdDate = createdDate,
  peerAddress = key1 ?: "null"
)

enum class LogType {
  INVALID_ADMIN_ACCESS,
  INVALID_ACCESS_CODE,
  PEER_REMOVED
}