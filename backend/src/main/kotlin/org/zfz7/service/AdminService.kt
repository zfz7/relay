package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.exchange.AdminDTO
import org.zfz7.repository.PeerRepository

@Service
class AdminService(
  val peerRepository: PeerRepository
) {
  fun getAdminPage() : AdminDTO {
    return AdminDTO(count = peerRepository.count())
  }
}