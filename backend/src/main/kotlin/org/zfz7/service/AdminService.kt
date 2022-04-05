package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.exchange.AdminDTO
import org.zfz7.repository.PeerRepository
import org.zfz7.security.PrincipalValidator
import java.security.Principal

@Service
class AdminService(
  val peerRepository: PeerRepository,
  val principalValidator: PrincipalValidator
) {
  fun getAdminPage(principal: Principal?) : AdminDTO {
    principalValidator.validate(principal)
    return AdminDTO(count = peerRepository.count())
  }
}