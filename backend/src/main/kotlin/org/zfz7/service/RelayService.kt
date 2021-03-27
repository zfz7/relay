package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.domain.Relay
import org.zfz7.repository.RelayRepository


@Service
class RelayService(
  val relayRepository: RelayRepository,
  val wgService: WgService
) {
  fun createRelay() {
    val privateKey = wgService.getPrivateKey()
    val relay = Relay(
      privateKey = privateKey,
      publicKey = wgService.getPublicKey(privateKey)
    )
    relayRepository.save(relay)
    wgService.writeRelayConfigFile()
  }
}