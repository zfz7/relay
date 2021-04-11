package org.zfz7.service

import javassist.NotFoundException
import org.springframework.stereotype.Service
import org.zfz7.domain.Peer
import org.zfz7.exchange.ConfFile
import org.zfz7.repository.PeerRepository
import org.zfz7.repository.RelayRepository
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class PeerService(
  val peerRepository: PeerRepository,
  val relayRepository: RelayRepository,
  val wgService: WgService
) {
  var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-YYYY")
    .withLocale(Locale.US)
    .withZone(ZoneOffset.UTC)

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
    val peer = peerRepository.findByPublicId(peerId) ?: throw NotFoundException("")
    val relay = relayRepository.findTopBy() ?: throw NotFoundException("")
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
      filename = "relay-expires-${formatter.format(peer.expiration)}.conf",
      file = targetStream,
      length = confByteArray.size.toLong()
    )
  }
}