package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.exchange.Peers
import org.zfz7.exchange.toDetailedDto
import org.zfz7.repository.PeerRepository
import org.zfz7.security.PrincipalValidator
import java.security.Principal

@Service
class AdminService(
  val peerRepository: PeerRepository,
  val principalValidator: PrincipalValidator,
) {
  fun getAdminPage(principal: Principal?) : Peers {
    principalValidator.validate(principal)
    return Peers(peers = peerRepository.findAll().map{it.toDetailedDto()})
  }
}