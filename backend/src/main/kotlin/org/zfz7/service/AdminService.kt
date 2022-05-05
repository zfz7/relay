package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.exchange.*
import org.zfz7.repository.PeerRepository
import org.zfz7.security.PrincipalValidator
import java.security.Principal

@Service
class AdminService(
  val peerRepository: PeerRepository,
  val principalValidator: PrincipalValidator,
  val logService: LogService,
  val codeService: CodeService,
  val configService: ConfigService
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

  fun getConfig(principal: Principal?): ConfigDTO {
    principalValidator.validate(principal)
    return configService.getConfig().toDto()
  }

  fun updateConfig(principal: Principal?, config: ConfigDTO) {
    principalValidator.validate(principal)
    configService.updateConfig(config)
  }
}