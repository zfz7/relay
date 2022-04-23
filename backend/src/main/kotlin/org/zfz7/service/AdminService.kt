package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.domain.LogType
import org.zfz7.domain.toInvalidAccessCodeEvent
import org.zfz7.domain.toInvalidAdminAccessEvent
import org.zfz7.domain.toPeerRemovedEvent
import org.zfz7.exchange.Logs
import org.zfz7.exchange.Peers
import org.zfz7.exchange.toDetailedDto
import org.zfz7.repository.LogEventRepository
import org.zfz7.repository.PeerRepository
import org.zfz7.security.PrincipalValidator
import java.security.Principal

@Service
class AdminService(
  val peerRepository: PeerRepository,
  val principalValidator: PrincipalValidator,
  val logEventRepository: LogEventRepository
) {
  fun getAdminPage(principal: Principal?) : Peers {
    principalValidator.validate(principal)
    return Peers(peers = peerRepository.findAll().map{it.toDetailedDto()})
  }
  fun getLogs(principal: Principal?): Logs {
    principalValidator.validate(principal)
    val logs = logEventRepository.findAll()
    return Logs(
      invalidAdminAccessEvents = logs.filter { it.logType == LogType.INVALID_ADMIN_ACCESS }.map{it.toInvalidAdminAccessEvent()},
      invalidAccessCodeEvents = logs.filter { it.logType == LogType.INVALID_ACCESS_CODE }.map{it.toInvalidAccessCodeEvent()},
      peerRemovedEvents = logs.filter { it.logType == LogType.PEER_REMOVED }.map{it.toPeerRemovedEvent()},
    )
  }
}