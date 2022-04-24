package org.zfz7.exchange

import org.zfz7.domain.InvalidAccessCodeEvent
import org.zfz7.domain.InvalidAdminAccessEvent
import org.zfz7.domain.PeerRemovedEvent

data class Logs(
  val invalidAdminAccessEvents: List<InvalidAdminAccessEvent> = emptyList(),
  val invalidAccessCodeEvents: List<InvalidAccessCodeEvent> = emptyList(),
  val peerRemovedEvents: List<PeerRemovedEvent> = emptyList()
)

