package org.zfz7.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.zfz7.domain.Peer
import org.zfz7.domain.PeerRemovedEvent
import org.zfz7.domain.toLogEvent
import org.zfz7.exchange.ConfFile
import org.zfz7.exchange.NotFoundException
import org.zfz7.repository.LogEventRepository
import org.zfz7.repository.PeerRepository
import org.zfz7.repository.RelayRepository
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class PeerService(
  val peerRepository: PeerRepository,
  val relayRepository: RelayRepository,
  val wgService: WgService,
  val logEventRepository: LogEventRepository
) {
  var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMM")
    .withLocale(Locale.US)
    .withZone(ZoneOffset.UTC)
  private val logger = LoggerFactory.getLogger(StartUp::class.java)

  fun createNewPeer(): Peer {
    val privateKey = wgService.getPrivateKey()
    val peer =  peerRepository.save(Peer(
      address = getNextAddress(),
      privateKey = privateKey,
      preSharedKey = wgService.getPreSharedKey(),
      publicKey = wgService.getPublicKey(privateKey)
    ))
    wgService.writeRelayConfigFile()
    return peer
  }

  private fun getNextAddress(): String {
    val peer = peerRepository.findAll().maxByOrNull { it.id!! } ?: return "10.0.0.2/32"//first client
    val parts: List<String> = peer.address.split(".","/")
    require(parts.size == 5)
    var value: Int = parts[2].toInt(10) shl 8 * 1 and 0x0000FF00 or (
            parts[3].toInt(10) shl 8 * 0 and 0x000000FF)
    if(value++ >= 65535)
      throw Exception("Too many clients")
    var nextIp = "10.0"
    for (i in 1 downTo 0) {
      nextIp += "."
      nextIp += value shr i * 8 and 0x000000FF
    }
    return "$nextIp/32"
  }

  fun getPeerConfig(peerId: UUID): ConfFile {
    val peer = peerRepository.findByPublicId(peerId) ?: throw NotFoundException("Peer not found")
    val relay = relayRepository.findTopBy() ?: throw NotFoundException("Relay not found")
    val confByteArray: ByteArray =
      ("[Interface]\n" +
              "Address = ${peer.address}\n" +
              "PrivateKey = ${peer.privateKey}\n" +
              "DNS = 10.0.0.1\n"+
              "[Peer]\n" +
              "AllowedIPs = ${peer.allowedIps}\n" +
              "Endpoint = ${peer.endPoint}\n" +
              "PresharedKey = ${peer.preSharedKey}\n" +
              "PublicKey = ${relay.publicKey}\n").toByteArray()
    val targetStream: InputStream = ByteArrayInputStream(confByteArray)
    return ConfFile(
      filename = "relayExp${formatter.format(peer.expiration)}.conf",
      file = targetStream
    )
  }

  @Scheduled(cron = "0 0 12 * * ?")//Every day at 12
  fun removeExpiredPeers() {
    val now = Instant.now()
    val peersToRemove = peerRepository.findAll().filter { it.expiration.isBefore(now) }
    if(peersToRemove.isNotEmpty()){
      peerRepository.deleteAll(peersToRemove)
      wgService.writeRelayConfigFile()
      logEventRepository.saveAll(peersToRemove.map{PeerRemovedEvent(peerAddress = it.address).toLogEvent()})
    }
    logger.info("${peersToRemove.size} peers removed")
  }
}