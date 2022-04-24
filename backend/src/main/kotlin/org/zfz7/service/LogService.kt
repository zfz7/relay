package org.zfz7.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.zfz7.domain.*
import org.zfz7.exchange.Logs
import org.zfz7.repository.LogEventRepository

@Service
class LogService(
  val logEventRepository: LogEventRepository,
  @Value("\${relay.disableLogs}")
  val disableLogs: Boolean
) {
  fun getLogs(): Logs {
    if(disableLogs) return Logs()
    val logs = logEventRepository.findAll()
    return Logs(
      invalidAdminAccessEvents = logs.filter { it.logType == LogType.INVALID_ADMIN_ACCESS }.map{it.toInvalidAdminAccessEvent()},
      invalidAccessCodeEvents = logs.filter { it.logType == LogType.INVALID_ACCESS_CODE }.map{it.toInvalidAccessCodeEvent()},
      peerRemovedEvents = logs.filter { it.logType == LogType.PEER_REMOVED }.map{it.toPeerRemovedEvent()},
    )
  }

  fun logRemovedPeers(peersToRemove: List<Peer>){
    if(disableLogs) return
    logEventRepository.saveAll(peersToRemove.map{ PeerRemovedEvent(peerAddress = it.address).toLogEvent()})
  }

  fun logInvalidAccessCodeEvent(ipAddress: String){
    if(disableLogs) return
    logEventRepository.save(InvalidAccessCodeEvent(ipAddress = ipAddress).toLogEvent())
  }

  fun logInvalidAdminAccessEvent(username: String){
    if(disableLogs) return
    logEventRepository.save(InvalidAdminAccessEvent(username = username).toLogEvent())
  }
}