package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.exchange.CodeDTO
import org.zfz7.exchange.Logs
import org.zfz7.exchange.Peers
import org.zfz7.exchange.toDetailedDto
import org.zfz7.repository.PeerRepository
import org.zfz7.security.PrincipalValidator
import java.security.Principal

@Service
class AdminService(
  val peerRepository: PeerRepository,
  val principalValidator: PrincipalValidator,
  val logService: LogService,
  val codeService: CodeService
) {
  fun getAdminPage(principal: Principal?): Peers {
    principalValidator.validate(principal)
    return Peers(peers = peerRepository.findAll().map { it.toDetailedDto() })
  }

  fun getLogs(principal: Principal?): Logs {
    principalValidator.validate(principal)
    return logService.getLogs()
  }

  fun updateCode(principal: Principal?, code: String) {
    principalValidator.validate(principal)
    codeService.updateCode(code)
  }

  fun getCode(principal: Principal?): CodeDTO {
    principalValidator.validate(principal)
    return CodeDTO(code = codeService.getCode()?:"")
  }
}