package org.zfz7.service

import javassist.NotFoundException
import org.springframework.stereotype.Service
import org.zfz7.domain.Peer
import org.zfz7.exchange.PeerDTO
import org.zfz7.exchange.toDto
import org.zfz7.repository.PeerRepository
import org.zfz7.repository.RelayRepository
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

@Service
class PeerService(
  val peerRepository: PeerRepository,
  val relayRepository: RelayRepository
) {
  fun createNewPeer(): PeerDTO {
    val privatKey = runBashCommand("wg genkey")
    val peer = Peer(
      address = getNextAddress(),
      privateKey = privatKey,
      preSharedKey = runBashCommand("wg genpsk"),
      publicKey = runBashCommand("echo $privatKey | wg genpsk")
    )
    return peerRepository.save(peer).toDto()
  }


  private fun runBashCommand(command: String): String {
    val isWindows = System.getProperty("os.name")
      .toLowerCase().startsWith("windows")
    val builder = ProcessBuilder()
    if (isWindows) {
      throw Exception("Windows not supported")
    } else {
      builder.command("sh", "-c", command)
    }
    builder.directory(File(System.getProperty("user.home")))
    val process = builder.start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val line1 = reader.readLine()
    val exitCode = process.waitFor()
    assert(exitCode == 0)
    return line1
  }

  private fun getNextAddress(): String {
    val peer = peerRepository.findAll().maxByOrNull { it.id!! } ?: return "10.8.0.2/32"//first client
    val addy = peer.address.subSequence(0, peer.address.length - 2).split('.')[3].toInt() + 1
    return "10.8.0.$addy/32"
  }

  fun getPeerConfig(peerId: UUID): File {
    val peer = peerRepository.findByPublicId(peerId) ?: throw NotFoundException("")
    val relay = relayRepository.findTopBy() ?: throw NotFoundException("")
    val peerConf = File("relay.conf")
    peerConf.writeText(
      "[Interface]\n" +
              "Address = ${peer.address}\n" +
              "PrivateKey = ${peer.privateKey}\n" +
              "[Peer]\n" +
              "AllowedIPs = ${peer.allowedIps}\n" +
              "Endpoint = ${peer.endPoint}\n" +
              "PresharedKey = ${peer.preSharedKey}\n" +
              "PublicKey = ${relay.publicKey}\n"
    )
    return peerConf
  }
}