package org.zfz7.service

import org.springframework.stereotype.Service
import org.zfz7.domain.*
import org.zfz7.exchange.ConfigDTO
import org.zfz7.repository.ConfigRepository
import java.time.Instant

@Service
class ConfigService(
  val configRepository: ConfigRepository,
) {
  fun getConfig(): Config {
    return configRepository.findAllByValidUntilIsNull().maxByOrNull { it.createdDate }?: Config(disableLogs = true, clientValidDuration = 0)
  }

  fun updateConfig(dto: ConfigDTO) {
    val now = Instant.now()
    val validConfig = configRepository.findAllByValidUntilIsNull()
    configRepository.saveAll(
      listOf(
        validConfig.map { it.copy(validUntil = now) },
        listOf(Config(disableLogs = dto.disableLogs, clientValidDuration = dto.clientValidDuration))
      ).flatten()
    )
  }
}