package org.zfz7.service

import javassist.NotFoundException
import org.springframework.stereotype.Service
import org.zfz7.domain.Peer
import org.zfz7.exchange.PeerDTO
import org.zfz7.exchange.toDto
import org.zfz7.repository.PeerRepository
import java.io.File
import java.util.*

@Service
class PeerService(
  val peerRepository: PeerRepository
) {
  fun createNewPeer(): PeerDTO {
    val peer = Peer(
      address = "10.8.0.3/24,fd42:42:42::3/64",
      dns = "10.8.0.1,fd42:42:42::1",
      privateKey = "ABC",
      preSharedKey = "DEF",
      publicKey = "GHI"
    )
    return peerRepository.save(peer).toDto()
  }

  fun getPeerConfig(peerId: UUID): File {
    val peer = peerRepository.findByPublicId(peerId)?: throw NotFoundException("")
    val peerConf = File("relay.conf")
    peerConf.writeText(
      "[Interface]\n"+
            "Address = ${peer.address}\n"+
            "DNS = ${peer.dns}\n"+
            "PrivateKey = ${peer.privateKey}\n"+
            "[Peer]\n"+
            "AllowedIPs = ${peer.allowedIps}\n"+
            "Endpoint = ${peer.endPoint}\n"+
            "PresharedKey = ${peer.preSharedKey}\n"+
            "PublicKey = ${peer.publicKey}\n")
    return peerConf
  }
}