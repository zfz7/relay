package org.zfz7.exchange

import org.zfz7.domain.InvalidAccessCodeEvent
import org.zfz7.domain.InvalidAdminAccessEvent
import org.zfz7.domain.PeerRemovedEvent

data class Logs(
  val invalidAdminAccessEvents: List<InvalidAdminAccessEvent>,
  val invalidAccessCodeEvents: List<InvalidAccessCodeEvent>,
  val peerRemovedEvents: List<PeerRemovedEvent>
)

