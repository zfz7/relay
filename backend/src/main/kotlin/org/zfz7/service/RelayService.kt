package org.zfz7.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.zfz7.domain.Relay
import org.zfz7.repository.RelayRepository


@Service
class RelayService(
  val relayRepository: RelayRepository,
  val wgService: WgService,
  @Value("\${relayConfig.wgPort}")
  val relayWgPort: Int,
) {
  fun createRelay() {
    val privateKey = wgService.getPrivateKey()
    val relay = Relay(
      privateKey = privateKey,
      publicKey = wgService.getPublicKey(privateKey),
      listenPort = relayWgPort.toString()
    )
    relayRepository.save(relay)
    wgService.writeRelayConfigFile()
  }
}